package com.foodorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.db.bean.Good;
import com.foodorder.runtime.RT;
import com.foodorder.util.BitmapLoader;
import com.foodorder.util.PhoneUtil;
import com.foodorder.widget.recycler.BaseRecyclerAdapter;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by guodong on 16/9/24.
 */

public class OrderGoodAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, Good> {
    private Context mContext;
    private NumberFormat nf;

    public OrderGoodAdapter(Context mContext, List<Good> mDataList) {
        super(mDataList);
        this.mContext = mContext;
        nf = NumberFormat.getCurrencyInstance(RT.locale);
        nf.setMaximumFractionDigits(RT.PRICE_NUM);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderGoodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_good, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position, Good data) {
        if (data == null) {
            return;
        }
        OrderGoodViewHolder mHolder = (OrderGoodViewHolder) holder;
        mHolder.bindData(data, nf);

    }

    public static class OrderGoodViewHolder extends BaseRecyclerViewHolder {
        ImageView img;
        TextView name, price, tv_code, tvCount;
        Good item;

        public OrderGoodViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.tvName);
            price = (TextView) itemView.findViewById(R.id.tvPrice);
            tv_code = (TextView) itemView.findViewById(R.id.tv_code);
            tvCount = (TextView) itemView.findViewById(R.id.count);
        }

        public void bindData(Good item, NumberFormat nf) {
            this.item = item;
            BitmapLoader.ins().loadImage(item.getImage_url(), R.drawable.ic_def_image, img);
            String good_name = "";
            if (PhoneUtil.isZh()) {
                good_name = item.getZh_name();
            } else {
                good_name = item.getFr_name();
            }
            name.setText(good_name);
            tv_code.setText(item.getReference());
            tvCount.setText(String.valueOf(item.getCount()));
            price.setText(nf.format(item.getPrice()));

        }
    }
}
