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

public class PackOrderAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, Order> {
    private Context mContext;
    private NumberFormat nf;

    public PackOrderAdapter(Context mContext, List<Order> mDataList) {
        super(mDataList);
        this.mContext = mContext;
        nf = NumberFormat.getCurrencyInstance(RT.locale);
        nf.setMaximumFractionDigits(RT.PRICE_NUM);
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
        PackViewHolder mHolder = (PackViewHolder) holder;
        mHolder.tv_order_num.setText(data.getNumber());
//        mHolder.tv_price.setText(mContext.getString(R.string.order_price, nf.format(data.getTotal())));
        mHolder.tv_price.setText(nf.format(data.getTotal()));
        mHolder.tv_time.setText(data.getTime());

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