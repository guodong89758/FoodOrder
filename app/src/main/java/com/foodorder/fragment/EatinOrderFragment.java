package com.foodorder.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodorder.R;
import com.foodorder.adapter.EatinOrderAdapter;
import com.foodorder.base.BaseFragment;
import com.foodorder.db.bean.Order;
import com.foodorder.runtime.WeakHandler;
import com.foodorder.widget.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class EatinOrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

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
}
