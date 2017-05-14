package com.foodorder.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.base.BaseRecyclerAdapter;
import com.foodorder.db.bean.GoodType;
import com.foodorder.logic.CartManager;
import com.foodorder.util.PhoneUtil;

import java.util.List;

public class TypeAdapter extends BaseRecyclerAdapter<TypeAdapter.TypeViewHolder, GoodType> {
    public int selectTypeId = 0;
    public Context mContext;
    public List<GoodType> dataList;

    public TypeAdapter(Context context, List<GoodType> dataList) {
        super(dataList);
        this.mContext = context;
    }


    @Override
    public TypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type, parent, false);
        TypeViewHolder holder = new TypeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TypeViewHolder holder, int position, GoodType data) {
        if (data == null) {
            return;
        }
        holder.bindData(data);
    }

//    @Override
//    public int getItemCount() {
//        if (dataList == null) {
//            return 0;
//        }
//        return dataList.size();
//    }

    public class TypeViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder {
        private View sign_view;
        TextView tvCount, type;

        public TypeViewHolder(View itemView) {
            super(itemView);
            sign_view = itemView.findViewById(R.id.sign_view);
            tvCount = (TextView) itemView.findViewById(R.id.tvCount);
            type = (TextView) itemView.findViewById(R.id.type);
        }

        public void bindData(GoodType item) {
            String type_name = "";
            if (PhoneUtil.isZh()) {
                type_name = item.getZh_name();
            } else {
                type_name = item.getFr_name();
            }
            type.setText(type_name);
            int count = CartManager.ins().getSelectedGroupCountByTypeId(item.getPosition());
            tvCount.setText(String.valueOf(count));
            if (count < 1) {
                tvCount.setVisibility(View.GONE);
            } else {
                tvCount.setVisibility(View.VISIBLE);
            }
            if (item.getPosition() == selectTypeId) {
                itemView.setBackgroundColor(Color.WHITE);
                sign_view.setVisibility(View.VISIBLE);
                type.setTextColor(ContextCompat.getColor(mContext, R.color.black_60));
                type.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.good_type_list_bg_color));
                sign_view.setVisibility(View.GONE);
                type.setTextColor(ContextCompat.getColor(mContext, R.color.black_50));
                type.setTypeface(Typeface.DEFAULT);
            }

        }

    }
}
