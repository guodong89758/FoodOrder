package com.foodorder.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.foodorder.R;
import com.foodorder.base.BaseActivity;
import com.foodorder.util.SmoothSwitchScreenUtil;

public class OrdersActivity extends BaseActivity {

    private FloatingActionButton fab_order;

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
}
