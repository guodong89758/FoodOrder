package com.foodorder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.entry.OrderInfo;
import com.foodorder.runtime.RT;
import com.foodorder.util.PhoneUtil;
import com.foodorder.widget.recycler.BaseRecyclerAdapter;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by guodong on 16/9/24.
 */

public class OrderVoucherAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, OrderInfo> {
    private Context mContext;
    private NumberFormat nf;

    public OrderVoucherAdapter(Context mContext, List<OrderInfo> mDataList) {
        super(mDataList);
        this.mContext = mContext;
        nf = NumberFormat.getCurrencyInstance(RT.locale);
        nf.setMaximumFractionDigits(RT.PRICE_NUM);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VoucherViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_voucher, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position, OrderInfo data) {
        if (data == null) {
            return;
        }
        VoucherViewHolder mHolder = (VoucherViewHolder) holder;
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RT.getScreenWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        mHolder.itemView.setLayoutParams(params);
        mHolder.bindData(data, nf);

    }

    public static class VoucherViewHolder extends BaseRecyclerViewHolder {
        TextView tv_count, tv_name, tv_price, tv_discount, tv_total_price;
        OrderInfo item;

        public VoucherViewHolder(View itemView) {
            super(itemView);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_discount = (TextView) itemView.findViewById(R.id.tv_discount);
            tv_total_price = (TextView) itemView.findViewById(R.id.tv_total_price);
        }

        public void bindData(OrderInfo item, NumberFormat nf) {
            this.item = item;
            String good_name = "";
            if (PhoneUtil.isZh()) {
                good_name = item.getProduct_name_zh();
            } else {
                good_name = item.getProduct_name();
            }
            tv_count.setText(String.valueOf(item.getProduct_quantity()));
            tv_name.setText(good_name);
            tv_price.setText(nf.format(item.getProduct_price()));
            if (!TextUtils.equals(item.getReduction(), "-0 %")) {
                tv_discount.setText(item.getReduction());
            } else {
                tv_discount.setText("");
            }
            tv_total_price.setText(nf.format(item.getProduct_total()));
        }
    }
}
