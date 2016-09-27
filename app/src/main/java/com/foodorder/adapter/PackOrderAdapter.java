package com.foodorder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.db.bean.Order;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by guodong on 16/9/24.
 */

public class PackOrderAdapter extends SwipeMenuAdapter<PackOrderAdapter.PackViewHolder> {
    private Context mContext;
    private List<Order> mDataList;

    public PackOrderAdapter(Context mContext, List<Order> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_pack, parent, false);
    }

    @Override
    public PackOrderAdapter.PackViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new PackOrderAdapter.PackViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(PackOrderAdapter.PackViewHolder holder, int position) {
        Order order = mDataList.get(position);
        if(order == null){
            return;
        }
    }

    public static class PackViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_order_num, tv_price, tv_time;

        public PackViewHolder(View itemView) {
            super(itemView);
            tv_order_num = (TextView) itemView.findViewById(R.id.tv_order_num);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}