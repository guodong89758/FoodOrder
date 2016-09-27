package com.foodorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.base.BaseRecyclerAdapter;
import com.foodorder.db.bean.Order;

import java.util.List;

/**
 * Created by guodong on 16/9/24.
 */

public class PackOrderAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, Order> {
    private Context mContext;

    public PackOrderAdapter(Context mContext, List<Order> mDataList) {
        super(mDataList);
        this.mContext = mContext;
    }

    @Override
    public BaseRecyclerAdapter.BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PackOrderAdapter.PackViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_pack, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position, Order data) {
        if (data == null) {
            return;
        }

    }

    public static class PackViewHolder extends BaseRecyclerViewHolder {
        public TextView tv_order_num, tv_price, tv_time;

        public PackViewHolder(View itemView) {
            super(itemView);
            tv_order_num = (TextView) itemView.findViewById(R.id.tv_order_num);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}