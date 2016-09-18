package com.foodorder.cell;

import android.widget.BaseAdapter;

public interface ListCell
{

    public void onGetData(Object data, int position, BaseAdapter adapter);

}
