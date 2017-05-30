package com.foodorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.entry.Bluetooth;

/**
 * Created by guodong on 2017/5/30.
 */

public class ToothAdapter extends FOAdapter {

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_bluetooth, null);
            holder.rl_title = (RelativeLayout) convertView.findViewById(R.id.rl_title);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Bluetooth tooth = (Bluetooth) getItem(position);
        if (tooth.isHasTitle()) {
            holder.rl_title.setVisibility(View.VISIBLE);
            holder.tv_type.setText(tooth.getType());
        } else {
            holder.rl_title.setVisibility(View.GONE);
        }
        holder.tv_title.setText(tooth.getTitle());
        holder.tv_address.setText(tooth.getAddress());
        return convertView;
    }

    static class ViewHolder {
        RelativeLayout rl_title;
        TextView tv_type, tv_title, tv_address;
    }
}
