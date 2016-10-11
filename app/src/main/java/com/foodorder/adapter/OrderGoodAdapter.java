package com.foodorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.db.bean.Good;
import com.foodorder.logic.CartManager;
import com.foodorder.runtime.RT;
import com.foodorder.util.PhoneUtil;

import java.text.NumberFormat;

/**
 * Created by guodong on 16/10/11.
 */

public class OrderGoodAdapter extends FOAdapter<Good> {
    private NumberFormat nf;
    private Context mContext;

    public OrderGoodAdapter(Context mContext) {
        this.mContext = mContext;
        nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(RT.PRICE_NUM);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderGoodAdapter.ItemViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_good, parent, false);
            holder = new OrderGoodAdapter.ItemViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (OrderGoodAdapter.ItemViewHolder) convertView.getTag();
        }
        Good item = data.get(position);
        holder.bindData(item);
        return convertView;
    }

    class ItemViewHolder {
        private ImageView img;
        private TextView name, price, tv_code, tvCount;
        private Good item;

        public ItemViewHolder(View itemView) {
            img = (ImageView) itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.tvName);
            price = (TextView) itemView.findViewById(R.id.tvPrice);
            tv_code = (TextView) itemView.findViewById(R.id.tv_code);
            tvCount = (TextView) itemView.findViewById(R.id.count);
        }

        public void bindData(Good item) {
            this.item = item;
            String good_name = "";
            if (PhoneUtil.isZh()) {
                good_name = item.getZh_name();
            } else {
                good_name = item.getFr_name();
            }
            name.setText(good_name);
            tv_code.setText(item.getReference());
            item.setCount(CartManager.ins().getSelectedItemCountById(item.getId().intValue()));
            tvCount.setText(String.valueOf(item.getCount()));
            price.setText(nf.format(item.getPrice()));

        }

    }

}
