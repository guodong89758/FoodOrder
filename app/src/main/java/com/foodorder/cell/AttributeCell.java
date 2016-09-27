package com.foodorder.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.db.bean.Attribute;

/**
 * Created by guodong on 16/9/24.
 */

public class AttributeCell extends LinearLayout implements ListCell, View.OnClickListener {
    private TextView tv_name, tv_add, tv_minus, tv_count;
    private Attribute attr;

    public AttributeCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_minus = (TextView) findViewById(R.id.tv_minus);
        tv_count = (TextView) findViewById(R.id.tv_count);

        tv_add.setOnClickListener(this);
        tv_minus.setOnClickListener(this);
    }

    @Override
    public void onGetData(Object data, int position, BaseAdapter adapter) {
        if (data == null) {
            return;
        }
        attr = (Attribute) data;
        String name = "";
//        if (PhoneUtil.isZh()) {
//            name = attr.getZh_name();
//        } else {
        name = attr.getFr_name();
//        }
        tv_name.setText(name);
        tv_count.setText(String.valueOf(attr.getCount()));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                if (attr.getCount() == attr.getMax_choose()) {
                    return;
                }
                attr.setCount(attr.getCount() + 1);
                tv_count.setText(String.valueOf(attr.getCount()));
                break;
            case R.id.tv_minus:
                if (attr.getCount() < 1) {
                    return;
                }
                attr.setCount(attr.getCount() - 1);
                tv_count.setText(String.valueOf(attr.getCount()));
                break;
        }
    }
}
