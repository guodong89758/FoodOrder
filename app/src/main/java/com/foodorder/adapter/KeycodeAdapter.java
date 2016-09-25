package com.foodorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.base.BaseRecyclerAdapter;
import com.foodorder.db.bean.Good;

import java.util.List;


/**
 * Created by guodong on 16/9/25.
 */

public class KeycodeAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder, Good> {

    private Context mContext;

    public KeycodeAdapter(Context mContext, List<Good> mDataList) {
        super(mDataList);
        this.mContext = mContext;
    }

    @Override
    public BaseRecyclerAdapter.BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CodeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_keybord_code, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerAdapter.BaseRecyclerViewHolder holder, int position, Good data) {
        if (data == null) {
            return;
        }
        CodeViewHolder codeHolder = (CodeViewHolder) holder;
        codeHolder.tv_code.setText(data.getReference());

    }


    public static class CodeViewHolder extends BaseRecyclerViewHolder {
        public TextView tv_code;

        public CodeViewHolder(View itemView) {
            super(itemView);
            tv_code = (TextView) itemView.findViewById(R.id.tv_code);
        }
    }
}
