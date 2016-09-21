package com.foodorder.activity;

import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.foodorder.R;
import com.foodorder.base.BaseActivity;
import com.foodorder.log.DLOG;
import com.foodorder.parse.AppInitParse;
import com.foodorder.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {

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

    }

    @Override
    public void initData() {
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
    }

    @Override
    public void onClick(View v) {

    }


}
