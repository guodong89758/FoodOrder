package com.foodorder.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.activity.GoodListActivity;
import com.foodorder.db.bean.GoodType;
import com.foodorder.util.PhoneUtil;

import java.util.List;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {
    public int selectTypeId = 0;
    public GoodListActivity activity;
    public List<GoodType> dataList;

    public TypeAdapter(GoodListActivity activity, List<GoodType> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GoodType item = dataList.get(position);

        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvCount, type;
        private GoodType item;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCount = (TextView) itemView.findViewById(R.id.tvCount);
            type = (TextView) itemView.findViewById(R.id.type);
            itemView.setOnClickListener(this);
        }

        public void bindData(GoodType item) {
            this.item = item;
            String type_name = "";
            if (PhoneUtil.isZh()) {
                type_name = item.getZh_name();
            } else {
                type_name = item.getFr_name();
            }
            type.setText(type_name);
            int count = activity.getSelectedGroupCountByTypeId(item.getPosition());
            tvCount.setText(String.valueOf(count));
            if (count < 1) {
                tvCount.setVisibility(View.GONE);
            } else {
                tvCount.setVisibility(View.VISIBLE);
            }
            if (item.getPosition() == selectTypeId) {
                itemView.setBackgroundColor(Color.WHITE);
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT);
            }

        }

        @Override
        public void onClick(View v) {
            activity.onTypeClicked(item.getPosition());
        }
    }
}
