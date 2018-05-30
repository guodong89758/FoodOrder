package com.foodorder.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.foodorder.R;
import com.foodorder.adapter.OrderAdapter;
import com.foodorder.base.BaseActivity;
import com.foodorder.contant.AppKey;
import com.foodorder.contant.EventTag;
import com.foodorder.fragment.OrderInfoFragment1;
import com.foodorder.runtime.event.EventListener;
import com.foodorder.runtime.event.EventManager;
import com.lzy.okhttputils.OkHttpUtils;

public class OrderInfoActivity extends BaseActivity {
    private static final String TAG = "OrderInfoActivity";
    private ImageButton ib_back;
    private TabLayout tab_layout;
    private ViewPager vp_order;
    private OrderAdapter orderAdapter;
    private String id_order;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_info;
    }

    @Override
    public void initView() {
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        tab_layout = (TabLayout) findViewById(R.id.tab_layout);
        vp_order = (ViewPager) findViewById(R.id.vp_order);

        tab_layout.setTabMode(TabLayout.MODE_FIXED);
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

        ib_back.setOnClickListener(this);
        EventManager.ins().registListener(EventTag.ACTIVITY_FINISH, eventListener);
    }

    @Override
    public void initData() {
        id_order = getIntent().getStringExtra(AppKey.ORDER_ID);

        orderAdapter = new OrderAdapter(getSupportFragmentManager());
        orderAdapter.addFragment(OrderInfoFragment1.newInstance(id_order), getString(R.string.order_tab_1));
        orderAdapter.addFragment(OrderInfoFragment1.newInstance(id_order), getString(R.string.order_tab_2));
        vp_order.setAdapter(orderAdapter);
        vp_order.setCurrentItem(0);
        tab_layout.setupWithViewPager(vp_order);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(TAG);
        EventManager.ins().removeListener(EventTag.ACTIVITY_FINISH, eventListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                onBackPressed();
                break;
        }
    }

    EventListener eventListener = new EventListener() {
        @Override
        public void handleMessage(int what, int arg1, int arg2, Object dataobj) {
            switch (what) {
                case EventTag.ACTIVITY_FINISH:
                    finish();
                    break;
            }
        }
    };


}
