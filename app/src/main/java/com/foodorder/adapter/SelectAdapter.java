package com.foodorder.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.activity.GoodListActivity;
import com.foodorder.db.bean.Good;
import com.foodorder.logic.CartManager;
import com.foodorder.util.PhoneUtil;

import java.text.NumberFormat;


public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {
    private GoodListActivity activity;
    private SparseArray<Good> dataList;
    private NumberFormat nf;
    private LayoutInflater mInflater;

    public SelectAdapter(GoodListActivity activity, SparseArray<Good> dataList) {
        this.activity = activity;
        this.dataList = dataList;
        nf = NumberFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_selected_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Good item = dataList.valueAt(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Good item;
        private TextView tvCost, tvCount, tvAdd, tvMinus, tvName, tvCode;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCode = (TextView) itemView.findViewById(R.id.tvCode);
            tvCost = (TextView) itemView.findViewById(R.id.tvCost);
            tvCount = (TextView) itemView.findViewById(R.id.count);
            tvMinus = (TextView) itemView.findViewById(R.id.tvMinus);
            tvAdd = (TextView) itemView.findViewById(R.id.tvAdd);
            tvMinus.setOnClickListener(this);
            tvAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvAdd:
                    CartManager.ins().add(item, true);
                    break;
                case R.id.tvMinus:
                    CartManager.ins().remove(item, true);
                    break;
                default:
                    break;
            }
        }

        public void bindData(Good item) {
            this.item = item;
            String good_name = "";
            if (PhoneUtil.isZh()) {
                good_name = item.getZh_name();
            } else {
                good_name = item.getFr_name();
            }
            tvName.setText(good_name);
            tvCode.setText(item.getReference());
            tvCost.setText(nf.format(item.getCount() * item.getPrice()));
            tvCount.setText(String.valueOf(item.getCount()));
        }
    }
}
