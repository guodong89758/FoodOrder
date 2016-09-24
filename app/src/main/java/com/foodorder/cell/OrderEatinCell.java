package com.foodorder.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

/**
 * Created by guodong on 16/9/24.
 */

public class OrderEatinCell extends RelativeLayout implements ListCell {

    public OrderEatinCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void onGetData(Object data, int position, BaseAdapter adapter) {

    }
}
