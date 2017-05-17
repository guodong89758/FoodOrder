package com.foodorder.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.db.bean.Attribute;
import com.foodorder.db.bean.Good;
import com.foodorder.util.PhoneUtil;
import com.foodorder.util.ToastUtil;

import java.util.List;

/**
 * Created by guodong on 16/9/24.
 */

public class AttributeCell extends LinearLayout implements ListCell, View.OnClickListener {
    private RelativeLayout rl_content;
    private TextView tv_name, tv_count;
    private ImageButton tv_add, tv_minus;
    private Attribute attr;
    private Good good;
    private int type;

    public AttributeCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_add = (ImageButton) findViewById(R.id.tv_add);
        tv_minus = (ImageButton) findViewById(R.id.tv_minus);
        tv_count = (TextView) findViewById(R.id.tv_count);

        rl_content.setOnClickListener(this);
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
        if (PhoneUtil.isZh()) {
            name = attr.getZh_name();
        } else {
            name = attr.getFr_name();
        }
        tv_name.setText(name);
        tv_count.setText(String.valueOf(attr.getCount()));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_content:
            case R.id.tv_add:
                add();
                break;
            case R.id.tv_minus:
                remove();
                break;
        }
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public void setType(int type) {
        this.type = type;
    }

    private void add() {
        if (attr.getSel_count() >= attr.getMax_choose()) {
            ToastUtil.showToast(getContext().getResources().getString(R.string.formula_max_count_desc, good.getMax_attributes_choose()));
            return;
        }
        attr.setCount(attr.getCount() + 1);
        tv_count.setText(String.valueOf(attr.getCount()));
        List<Attribute> attrData = good.getAttributeList();
        for (int i = 0; i < attrData.size(); i++) {
            Attribute temp = attrData.get(i);
            temp.setSel_count(temp.getSel_count() + 1);
        }

    }

    private void remove() {
//        if (attr.getCount() < 1) {
//            return;
//        }
        attr.setCount(attr.getCount() - 1);
        tv_count.setText(String.valueOf(attr.getCount()));
        List<Attribute> attrData = good.getAttributeList();
        for (int i = 0; i < attrData.size(); i++) {
            Attribute temp = attrData.get(i);
            temp.setSel_count(temp.getSel_count() - 1);
        }

    }
}
