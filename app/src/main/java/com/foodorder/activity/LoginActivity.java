package com.foodorder.activity;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.foodorder.R;
import com.foodorder.base.BaseActivity;
import com.foodorder.logic.UserManager;
import com.foodorder.server.api.API_Food;
import com.foodorder.server.callback.JsonResponseCallback;
import com.foodorder.util.ToastUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.lzy.okhttputils.OkHttpUtils;

import org.json.JSONObject;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private AutoCompleteTextView tv_username;
    private EditText et_password;
    private Button btn_login, btn_zxing;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        tv_username = (AutoCompleteTextView) findViewById(R.id.tv_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_zxing = (Button) findViewById(R.id.btn_zxing);

        btn_login.setOnClickListener(this);
        btn_zxing.setOnClickListener(this);

    }

    @Override
    public void initData() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, UserManager.getInstance().getUserList());
        tv_username.setAdapter(arrayAdapter);
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
                API_Food.ins().login(TAG, username, password, new JsonResponseCallback() {
                    @Override
                    public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
                        if (errcode == 200) {
                            startActivity(new Intent(LoginActivity.this, OrdersActivity.class));
                            finish();
                        }
                        ToastUtil.showToast(errmsg);
                        return false;
                    }
                });

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
        }
    }


}
