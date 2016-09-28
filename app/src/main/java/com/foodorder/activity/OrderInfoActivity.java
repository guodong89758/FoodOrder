package com.foodorder.activity;

import android.view.View;
import android.widget.ImageButton;

import com.foodorder.R;
import com.foodorder.base.BaseActivity;

public class OrderInfoActivity extends BaseActivity {
    private ImageButton ib_back;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_info;
    }

    @Override
    public void initView() {
        ib_back = (ImageButton) findViewById(R.id.ib_back);

        ib_back.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                onBackPressed();
                break;
        }
    }
}
