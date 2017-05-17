package com.foodorder.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.foodorder.R;
import com.foodorder.adapter.OrderAdapter;
import com.foodorder.base.BaseActivity;
import com.foodorder.contant.AppKey;
import com.foodorder.fragment.EatinOrderFragment;
import com.foodorder.fragment.PackOrderFragment;
import com.foodorder.logic.CartManager;
import com.foodorder.pop.OrderSetupPop;
import com.foodorder.runtime.ActivityManager;
import com.foodorder.util.SmoothSwitchScreenUtil;
import com.foodorder.util.ToastUtil;

public class OrdersActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private Toolbar toolbar;
    private ImageButton ib_search;
    private TabLayout tab_layout;
    private ViewPager vp_order;
    private FloatingActionButton fab_menu;
    private OrderAdapter orderAdapter;
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ib_search = (ImageButton) findViewById(R.id.ib_search);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        vp_order = (ViewPager) findViewById(R.id.vp_order);
        fab_menu = (FloatingActionButton) findViewById(R.id.fab_menu);

        setSupportActionBar(toolbar);
        tab_layout.setTabMode(TabLayout.MODE_FIXED);
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
        ib_search.setOnClickListener(this);
        fab_menu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        orderAdapter = new OrderAdapter(getSupportFragmentManager());
        orderAdapter.addFragment(new EatinOrderFragment(), getString(R.string.order_tab_1));
        orderAdapter.addFragment(new PackOrderFragment(), getString(R.string.order_tab_2));
        vp_order.setAdapter(orderAdapter);
        vp_order.setCurrentItem(0);
        tab_layout.setupWithViewPager(vp_order);
        vp_order.addOnPageChangeListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_search:
                if (vp_order.getCurrentItem() == 0) {
                    startActivity(new Intent(this, OrderEatinSearchActivity.class));
                } else {
                    startActivity(new Intent(this, OrderPackSearchActivity.class));
                }
                break;
            case R.id.fab_menu:
                if (vp_order.getCurrentItem() == 0) {
                    CartManager.ins().isPack = false;
                } else {
                    CartManager.ins().isPack = true;
                }
                OrderSetupPop setupPop = new OrderSetupPop(OrdersActivity.this);
                setupPop.showPopup();
                setupPop.setOnOrderSetupListener(new OrderSetupPop.OnOrderSetupListener() {
                    @Override
                    public void orderSetup(String number, String persons) {
                        Intent intent = null;
//                        if (PhoneUtil.isPad(OrdersActivity.this)) {
                            intent = new Intent(OrdersActivity.this, GoodListPadActivity1.class);
//                        } else {
//                            intent = new Intent(OrdersActivity.this, GoodListActivity.class);
//                        }
                        intent.putExtra(AppKey.GOOD_LIST_TYPE, AppKey.GOOD_LIST_MENU);
                        intent.putExtra(AppKey.ORDER_NUMBER, number);
                        intent.putExtra(AppKey.ORDER_PERSON, persons);
                        startActivity(intent);
                    }
                });
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
