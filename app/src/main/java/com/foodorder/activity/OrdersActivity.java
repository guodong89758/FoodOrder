package com.foodorder.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.foodorder.R;
import com.foodorder.base.BaseActivity;
import com.foodorder.runtime.ActivityManager;
import com.foodorder.util.SmoothSwitchScreenUtil;
import com.foodorder.util.ToastUtil;

public class OrdersActivity extends BaseActivity {

    private FloatingActionButton fab_order;
    private long last_back_time = 0;

    @Override
    protected void onBeforeSetContentLayout() {
        super.onBeforeSetContentLayout();
        SmoothSwitchScreenUtil.smoothSwitchScreen(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_orders;
    }

    @Override
    public void initView() {
        fab_order = (FloatingActionButton) findViewById(R.id.fab_order);

        fab_order.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_order:
                startActivity(new Intent(OrdersActivity.this, GoodListActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {

        long current_time = System.currentTimeMillis();
        if (current_time - last_back_time > 2000) {
            ToastUtil.showToast(getString(R.string.app_exit));
            last_back_time = current_time;
        } else {
            ActivityManager.ins().AppExit(this);
        }

    }
}
