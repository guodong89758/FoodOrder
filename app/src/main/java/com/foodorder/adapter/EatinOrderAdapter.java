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

public class EatinOrderAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, Order> {
    private Context mContext;

    public EatinOrderAdapter(Context mContext, List<Order> mDataList) {
        super(mDataList);
        this.mContext = mContext;
    }

    @Override
    public BaseRecyclerAdapter.BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EatinViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_eatin, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position, Order data) {
        if (data == null) {
            return;
        }
    }

    public static class EatinViewHolder extends BaseRecyclerViewHolder {
        public TextView tv_taihao, tv_person_count, tv_price, tv_time;

        public EatinViewHolder(View itemView) {
            super(itemView);
            tv_taihao = (TextView) itemView.findViewById(R.id.tv_taihao);
            tv_person_count = (TextView) itemView.findViewById(R.id.tv_person_count);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
