package com.foodorder.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodorder.R;
import com.foodorder.activity.OrderInfoActivity;
import com.foodorder.adapter.EatinOrderAdapter;
import com.foodorder.base.BaseFragment;
import com.foodorder.base.BaseRecyclerAdapter;
import com.foodorder.db.bean.Order;
import com.foodorder.dialog.OrderActionDialog;
import com.foodorder.runtime.WeakHandler;
import com.foodorder.util.ToastUtil;
import com.foodorder.widget.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class EatinOrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseRecyclerAdapter.OnItemClickListener, BaseRecyclerAdapter.OnItemLongClickListener {

    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView rv_eatin;
    private EatinOrderAdapter orderAdapter;
    private List<Order> orderData;


    public EatinOrderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_eatin, container, false);
        swipe_refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swip_refresh);
        rv_eatin = (RecyclerView) rootView.findViewById(R.id.rv_eatin);

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
        rv_eatin.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_eatin.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).color(getResources().getColor(R.color.black_10)).size(1).build());
        rv_eatin.setHasFixedSize(true);
        orderAdapter = new EatinOrderAdapter(getActivity(), orderData);
        orderAdapter.setOnItemClickListener(this);
        orderAdapter.setOnItemLongClickListener(this);
        rv_eatin.setAdapter(orderAdapter);
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

    @Override
    public void onItemClick(View view, int position, long id) {
        startActivity(new Intent(getActivity(), OrderInfoActivity.class));
    }

    @Override
    public boolean onItemLongClick(View view, int position, long id) {
        Order order = orderData.get(position);
        if (order != null) {
            showActionDialog(order);
        }
        return true;
    }

    private void showActionDialog(Order order) {
        OrderActionDialog dialog = new OrderActionDialog(getActivity(), order);
        dialog.setButton1(new OrderActionDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, OrderActionDialog dialog, Order order) {
                ToastUtil.showToast(getResources().getString(R.string.order_action_1));
            }
        });
        dialog.setButton2(new OrderActionDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, OrderActionDialog dialog, Order order) {
                ToastUtil.showToast(getResources().getString(R.string.order_action_2));
            }
        });
        dialog.show();
    }

}
