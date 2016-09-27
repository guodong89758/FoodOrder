package com.foodorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foodorder.R;
import com.foodorder.cell.FormulaCell;

/**
 * Created by guodong on 16/9/27.
 */

public class FormulaAdapter extends FOAdapter {

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_formula, null);
        }
        if (convertView instanceof FormulaCell) {
            FormulaCell cell = (FormulaCell) convertView;
            cell.onGetData(getItem(position), position, this);
        }
        return convertView;
    }
}
