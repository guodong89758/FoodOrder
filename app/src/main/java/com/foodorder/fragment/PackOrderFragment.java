package com.foodorder.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodorder.R;
import com.foodorder.adapter.PackOrderAdapter;
import com.foodorder.base.BaseFragment;
import com.foodorder.db.bean.Order;
import com.foodorder.runtime.WeakHandler;
import com.foodorder.util.PhoneUtil;
import com.foodorder.widget.HorizontalDividerItemDecoration;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.foodorder.R.id.rv_eatin;


public class PackOrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipe_refresh;
    private SwipeMenuRecyclerView rv_pack;
    private PackOrderAdapter orderAdapter;
    private List<Order> orderData;

    public PackOrderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pack, container, false);
        swipe_refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swip_refresh);
        rv_pack = (SwipeMenuRecyclerView) rootView.findViewById(R.id.rv_pack);
        rv_pack.setLongPressDragEnabled(true);
        rv_pack.setSwipeMenuCreator(swipeMenuCreator);

        swipe_refresh.setColorSchemeResources(R.color.refresh_progress_blue, R.color.refresh_progress_green, R.color.refresh_progress_red, R.color.refresh_progress_yellow);
        swipe_refresh.setOnRefreshListener(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (orderData == null) {
            orderData = new ArrayList<>();
        }
        for (int i = 0; i < 15; i++) {
            orderData.add(new Order());
        }
        rv_pack.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_pack.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).color(getResources().getColor(R.color.black_10)).size(1).build());
        rv_pack.setHasFixedSize(true);

        orderAdapter = new PackOrderAdapter(getActivity(), orderData);
        rv_pack.setAdapter(orderAdapter);
    }

    @Override
    public void onRefresh() {
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipe_refresh.setRefreshing(false);
            }
        }, 3000);
    }

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            SwipeMenuItem deleteItem = new SwipeMenuItem(getContext())
                    .setBackgroundColor(getResources().getColor(R.color.colorAccent))
                    .setText("催单") // 文字。
                    .setTextColor(Color.WHITE) // 文字颜色。
                    .setTextSize(16) // 文字大小。
                    .setWidth(PhoneUtil.dipToPixel(60, getContext()))
                    .setHeight(PhoneUtil.dipToPixel(60, getContext()));
            swipeRightMenu.addMenuItem(deleteItem);
            SwipeMenuItem printItem = new SwipeMenuItem(getContext())
                    .setBackgroundColor(getResources().getColor(R.color.red_color))
                    .setText("打印") // 文字。
                    .setTextColor(Color.WHITE) // 文字颜色。
                    .setTextSize(16) // 文字大小。
                    .setWidth(PhoneUtil.dipToPixel(60, getContext()))
                    .setHeight(PhoneUtil.dipToPixel(60, getContext()));
            swipeRightMenu.addMenuItem(printItem);

        }
    };

}
