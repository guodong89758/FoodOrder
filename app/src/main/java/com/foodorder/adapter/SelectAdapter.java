package com.foodorder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.db.bean.Good;
import com.foodorder.logic.CartManager;
import com.foodorder.runtime.RT;
import com.foodorder.util.PhoneUtil;
import com.foodorder.widget.DefaultItemTouchHelper;

import java.text.NumberFormat;
import java.util.List;


public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {
    private Context mContext;
    private List<Good> dataList;
    private NumberFormat nf;
    private LayoutInflater mInflater;
    private ItemTouchHelper itemTouchHelper;

    public SelectAdapter(Context context, List<Good> dataList) {
        this.mContext = context;
        this.dataList = dataList;
        nf = NumberFormat.getCurrencyInstance(RT.locale);
        nf.setMaximumFractionDigits(RT.PRICE_NUM);
        mInflater = LayoutInflater.from(mContext);
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }

    public DefaultItemTouchHelper getItemTouchHelper() {
        return (DefaultItemTouchHelper) itemTouchHelper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_selected_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Good item = dataList.get(position);
        if (item == null) {
            return;
        }
        holder.bindData(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(position, item);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private Good item;
        private TextView tvCost, tvCount, tvName, tvCode;
        private ImageButton tvAdd, tvMinus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCode = (TextView) itemView.findViewById(R.id.tvCode);
            tvCost = (TextView) itemView.findViewById(R.id.tvCost);
            tvCount = (TextView) itemView.findViewById(R.id.count);
            tvMinus = (ImageButton) itemView.findViewById(R.id.tvMinus);
            tvAdd = (ImageButton) itemView.findViewById(R.id.tvAdd);
            tvMinus.setOnClickListener(this);
            tvAdd.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvAdd:
//                    CartManager.ins().add(item, true);
                    CartManager.ins().cartAdd(item, true);
                    break;
                case R.id.tvMinus:
//                    CartManager.ins().remove(item, true);
                    CartManager.ins().cartRemove(item, true);
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


        @Override
        public boolean onLongClick(View v) {
            if (itemView == v) {
                itemTouchHelper.startDrag(this);
            }
            return false;
        }
    }

    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, Good good);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

}
