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

public class EatinOrderAdapter extends SwipeMenuAdapter<EatinOrderAdapter.EatinViewHolder> {
    private Context mContext;
    private List<Order> mDataList;

    public EatinOrderAdapter(Context mContext, List<Order> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_eatin, parent, false);
    }

    @Override
    public EatinViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new EatinViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(EatinViewHolder holder, int position) {
        Order order = mDataList.get(position);
        if (order == null) {
            return;
        }
    }

    public static class EatinViewHolder extends RecyclerView.ViewHolder {
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
