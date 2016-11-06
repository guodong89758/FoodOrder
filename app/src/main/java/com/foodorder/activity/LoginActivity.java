package com.foodorder.activity;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.base.BaseActivity;
import com.foodorder.contant.AppKey;
import com.foodorder.logic.UserManager;
import com.foodorder.pop.LoginUserPop;
import com.foodorder.runtime.RT;
import com.foodorder.runtime.WeakHandler;
import com.foodorder.server.api.API_Food;
import com.foodorder.server.callback.JsonResponseCallback;
import com.foodorder.util.PhoneUtil;
import com.foodorder.util.PreferenceHelper;
import com.foodorder.util.SoftKeyboardUtil;
import com.foodorder.util.ToastUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.lzy.okhttputils.OkHttpUtils;

import org.json.JSONObject;

import java.util.Locale;


public class LoginActivity extends BaseActivity implements LoginUserPop.OnUserSelectedListener {
    private static final String TAG = "LoginActivity";
    //    private AutoCompleteTextView tv_username;
    private TextView tv_username;
    private EditText et_password;
    private Button btn_login, btn_zxing;
    private RadioGroup rg_language;
    private RadioButton rb_zh, rb_fr;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
//        tv_username = (AutoCompleteTextView) findViewById(tv_username);
        tv_username = (TextView) findViewById(R.id.tv_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_zxing = (Button) findViewById(R.id.btn_zxing);
        rg_language = (RadioGroup) findViewById(R.id.rg_language);
        rb_zh = (RadioButton) findViewById(R.id.rb_zh);
        rb_fr = (RadioButton) findViewById(R.id.rb_fr);

        btn_login.setOnClickListener(this);
        btn_zxing.setOnClickListener(this);
        tv_username.setOnClickListener(this);

        rg_language.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String language = PreferenceHelper.ins().getStringShareData(AppKey.LANGUAGE, "zh");
                switch (checkedId) {
                    case R.id.rb_zh:
                        if ("zh".equals(language)) {
                            return;
                        }
                        switchLanguage("zh");
                        break;
                    case R.id.rb_fr:
                        if ("fr".equals(language)) {
                            return;
                        }
                        switchLanguage("fr");
                        break;
                }
            }
        });

    }

    @Override
    public void initData() {
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, UserManager.getInstance().getUserList());
//        tv_username.setAdapter(arrayAdapter);
        String language = PreferenceHelper.ins().getStringShareData(AppKey.LANGUAGE, PhoneUtil.isZh() ? "zh" : "fr");
        if (language.equals("zh")) {
            rb_zh.setChecked(true);
        } else {
            rb_fr.setChecked(true);
        }

        if (UserManager.getInstance().getUserList().size() == 1) {
            tv_username.setText(UserManager.getInstance().getUserList().get(0));
        } else {
            tv_username.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (tv_username != null) {
                        tv_username.performClick();
                    }
                }
            }, 500);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(TAG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                SoftKeyboardUtil.hideSoftKeyboard(et_password);
                String username = tv_username.getText().toString();
                String password = et_password.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    ToastUtil.showToast(getString(R.string.login_username_empty));
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.showToast(getString(R.string.login_password_empty));
                    return;
                }
                if (RT.DEBUG) {
                    startActivity(new Intent(LoginActivity.this, OrdersActivity.class));
                    finish();
                } else {
                    API_Food.ins().login(TAG, username, password, new JsonResponseCallback() {
                        @Override
                        public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
                            if (errcode == 200) {
                                startActivity(new Intent(LoginActivity.this, OrdersActivity.class));
                                finish();
                                ToastUtil.showToast(getString(R.string.login_success));
                            } else {
                                ToastUtil.showToast(getString(R.string.login_failed));
                            }
                            return false;
                        }
                    });
                }

                break;
            case R.id.btn_zxing:
                Dexter.checkPermission(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startActivity(new Intent(LoginActivity.this, ScanActivity.class));
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }, Manifest.permission.CAMERA);
                break;
            case R.id.tv_username:
                if (UserManager.getInstance().getUserList() != null && UserManager.getInstance().getUserList().size() > 1) {
                    LoginUserPop userPop = new LoginUserPop(this);
                    userPop.setOnUserSelectedListener(this);
                    userPop.showPopup(tv_username);
                }
                break;
        }
    }


    @Override
    public void selectedUser(String username) {
        tv_username.setText(username);
        et_password.requestFocus();
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et_password.requestFocus();
                SoftKeyboardUtil.showSoftKeyboard(et_password);
            }
        }, 500);

    }


    protected void switchLanguage(String language) {

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        switch (language) {
            case "fr":
                config.locale = Locale.FRANCE;
                resources.updateConfiguration(config, dm);
                PreferenceHelper.ins().storeShareStringData(AppKey.LANGUAGE, "fr");
                PreferenceHelper.ins().commit();
                break;
            case "zh":
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
                PreferenceHelper.ins().storeShareStringData(AppKey.LANGUAGE, "zh");
                PreferenceHelper.ins().commit();
                break;
            default:
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
                break;
        }

        Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
