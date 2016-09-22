package com.foodorder.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.foodorder.R;
import com.foodorder.base.BaseActivity;
import com.foodorder.logic.UserManager;

public class LoginActivity extends BaseActivity {

    private AutoCompleteTextView tv_username;
    private EditText et_password;
    private Button btn_login;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        tv_username = (AutoCompleteTextView) findViewById(R.id.tv_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);

    }

    @Override
    public void initData() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, UserManager.getInstance().getUserList());
        tv_username.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(LoginActivity.this, OrdersActivity.class));
                finish();
                break;
        }
    }


}
