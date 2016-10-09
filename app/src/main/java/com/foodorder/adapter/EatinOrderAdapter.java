package com.foodorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.base.BaseRecyclerAdapter;
import com.foodorder.db.bean.Order;
import com.foodorder.runtime.RT;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by guodong on 16/9/24.
 */

public class EatinOrderAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, Order> {
    private Context mContext;
    private NumberFormat nf;

    public EatinOrderAdapter(Context mContext, List<Order> mDataList) {
        super(mDataList);
        this.mContext = mContext;
        nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(RT.PRICE_NUM);
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
        EatinViewHolder mHolder = (EatinViewHolder) holder;
        mHolder.tv_taihao.setText(data.getNumber());
        mHolder.tv_person_count.setText(String.valueOf(data.getPersons()));
        mHolder.tv_price.setText(nf.format(data.getTotal()));
        mHolder.tv_time.setText(data.getTime());

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
