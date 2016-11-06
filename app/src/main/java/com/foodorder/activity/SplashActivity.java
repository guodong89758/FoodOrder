package com.foodorder.activity;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.foodorder.R;
import com.foodorder.base.BaseActivity;
import com.foodorder.contant.AppKey;
import com.foodorder.contant.EventTag;
import com.foodorder.log.DLOG;
import com.foodorder.parse.AppInitParse;
import com.foodorder.runtime.RT;
import com.foodorder.runtime.event.EventListener;
import com.foodorder.runtime.event.EventManager;
import com.foodorder.server.ServerManager;
import com.foodorder.server.api.API_Food;
import com.foodorder.server.callback.JsonResponseCallback;
import com.foodorder.util.PhoneUtil;
import com.foodorder.util.PreferenceHelper;
import com.foodorder.util.StringUtil;
import com.foodorder.util.ToastUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.lzy.okhttputils.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";

    @Override
    protected void onBeforeSetContentLayout() {
        super.onBeforeSetContentLayout();
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        EventManager.ins().registListener(EventTag.GET_SERVER_DOMAIN_FROM_ZXING, eventListener);
        switchLanguage(PreferenceHelper.ins().getStringShareData(AppKey.LANGUAGE, PhoneUtil.isZh() ? "zh" : "fr"));
    }

    @Override
    public void initData() {
        if (RT.DEBUG) {
            Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    String menu_json = StringUtil.getJson(SplashActivity.this, "menu.json");
                    DLOG.json(menu_json);
                    JSONObject json = null;
                    try {
                        json = new JSONObject(menu_json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AppInitParse.parseJson(json);

                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
                @Override
                public void onCompleted() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }

                @Override
                public void onError(Throwable e) {
                    DLOG.e(e.getMessage());
                }

                @Override
                public void onNext(Object o) {

                }
            });
        } else {
            if (StringUtil.checkTime()) {
                ServerManager.SERVER_DOMAIN = PreferenceHelper.ins().getStringShareData(AppKey.SERVER_DOMAIN, "");
                if (TextUtils.isEmpty(ServerManager.SERVER_DOMAIN)) {
                    Dexter.checkPermission(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            startActivity(new Intent(SplashActivity.this, ScanActivity.class));
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            if (token != null) {
                                token.continuePermissionRequest();
                            }
                        }
                    }, Manifest.permission.CAMERA);
                } else {
                    API_Food.ins().getGoodMenu(TAG, initCallbck);
                }
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.ins().removeListener(EventTag.GET_SERVER_DOMAIN_FROM_ZXING, eventListener);
        OkHttpUtils.getInstance().cancelTag(TAG);
    }

    @Override
    public void onClick(View v) {

    }

    EventListener eventListener = new EventListener() {
        @Override
        public void handleMessage(int what, int arg1, int arg2, Object dataobj) {
            switch (what) {
                case EventTag.GET_SERVER_DOMAIN_FROM_ZXING:
                    API_Food.ins().getGoodMenu(TAG, initCallbck);
                    break;
            }
        }
    };

    JsonResponseCallback initCallbck = new JsonResponseCallback() {
        @Override
        public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
            if (errcode == 200 && json != null) {
                praseJson(json);
            } else {
                ToastUtil.showToast(errmsg);
                Dexter.checkPermission(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startActivity(new Intent(SplashActivity.this, ScanActivity.class));
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        if (token != null) {
                            token.continuePermissionRequest();
                        }
                    }
                }, Manifest.permission.CAMERA);

            }
            return false;
        }
    };

    private void praseJson(final JSONObject json) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                AppInitParse.parseJson(json);
                RT.ins().getDaoSession().getOrderDao().deleteAll();
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onCompleted() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onError(Throwable e) {
                DLOG.e(e.getMessage());
            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    protected void switchLanguage(String language) {

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        switch (language) {
            case "fr":
                config.locale = Locale.FRANCE;
                resources.updateConfiguration(config, dm);
                break;
            case "zh":
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
                break;
            default:
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
                break;
        }

    }
}
