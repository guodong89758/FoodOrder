package com.foodorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodorder.R;
import com.foodorder.cell.AttributeCell;
import com.foodorder.db.bean.Good;

/**
 * Created by guodong on 16/9/27.
 */

public class AttributeAdapter extends FOAdapter {

    private Good good;
    private int type;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attribute, null);
        }
        if (convertView instanceof AttributeCell) {
            AttributeCell cell = (AttributeCell) convertView;
            cell.setType(type);
            cell.setGood(good);
            cell.onGetData(getItem(position), position, this);
        }
        return convertView;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public void setType(int type) {
        this.type = type;
    }
}
