package com.foodorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.entry.OrderInfo;
import com.foodorder.runtime.RT;
import com.foodorder.util.PhoneUtil;

import java.text.NumberFormat;

/**
 * Created by guodong on 16/10/11.
 */

public class OrderVoucherAdapter extends FOAdapter<OrderInfo> {
    private NumberFormat nf;
    private Context mContext;

    public OrderVoucherAdapter(Context mContext) {
        this.mContext = mContext;
        nf = NumberFormat.getCurrencyInstance(RT.locale);
        nf.setMaximumFractionDigits(RT.PRICE_NUM);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderVoucherAdapter.ItemViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_voucher, parent, false);
            holder = new OrderVoucherAdapter.ItemViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (OrderVoucherAdapter.ItemViewHolder) convertView.getTag();
        }
        OrderInfo item = data.get(position);
        holder.bindData(item);
        return convertView;
    }

    class ItemViewHolder {
        private TextView tv_count, tv_name, tv_price, tv_discount, tv_total_price;
        private OrderInfo item;

        public ItemViewHolder(View itemView) {
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_discount = (TextView) itemView.findViewById(R.id.tv_discount);
            tv_total_price = (TextView) itemView.findViewById(R.id.tv_total_price);
        }

        public void bindData(OrderInfo item) {
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
            tv_discount.setText(item.getReduction());
            tv_total_price.setText(nf.format(item.getProduct_quantity()*item.getProduct_price()));

        }

    }

}
