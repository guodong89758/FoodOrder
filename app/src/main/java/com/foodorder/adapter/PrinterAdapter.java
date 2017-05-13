package com.foodorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.base.BaseRecyclerAdapter;
import com.foodorder.contant.AppKey;
import com.foodorder.entry.Printer;

import java.util.List;

/**
 * Created by guodong on 16/9/24.
 */

public class PrinterAdapter extends BaseRecyclerAdapter<PrinterAdapter.PrinterViewHolder, Printer> {
    private Context mContext;
    private int fromType;

    public PrinterAdapter(Context mContext, List<Printer> mDataList, int fromType) {
        super(mDataList);
        this.mContext = mContext;
        this.fromType = fromType;
    }

    @Override
    public PrinterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PrinterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_printer, null));
    }

    @Override
    public void onBindViewHolder(PrinterViewHolder holder, int position, Printer data) {
        if (data == null) {
            return;
        }
        holder.tv_printer.setText("打印机" + position);
        if (fromType == AppKey.PRINTER_DAYIN) {
            holder.iv_check.setVisibility(View.GONE);
        } else {
            holder.iv_check.setVisibility(View.VISIBLE);
            if (data.isChecked()) {
                holder.iv_check.setImageResource(R.drawable.ic_check_yes);
            } else {
                holder.iv_check.setImageResource(R.drawable.ic_check_no);
            }
        }


    }

    public static class PrinterViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder {
        TextView tv_printer;
        ImageView iv_check;

        public PrinterViewHolder(View itemView) {
            super(itemView);
            tv_printer = (TextView) itemView.findViewById(R.id.tv_printer);
            iv_check = (ImageView) itemView.findViewById(R.id.iv_check);
        }
    }
}
