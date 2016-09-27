package com.foodorder.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.foodorder.R;
import com.foodorder.base.BaseActivity;

public class OrderEatinSearchActivity extends BaseActivity {
    private ImageButton ib_back, ib_search, ib_clear;
    private EditText et_search;
    private RecyclerView rv_order;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_eatin_search;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
