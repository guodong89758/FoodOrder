package com.foodorder.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.db.bean.Order;


public class OrderActionDialog extends Dialog implements View.OnClickListener {

    private DialogButtonOnClickListener listener_1, listener_2, listener_3;
    private Context context;
    private TextView tv_cuidan, tv_print, tv_hang;
    private Order order;

    public OrderActionDialog(Context context, Order order) {
        super(context, R.style.NormalDialog);
        setCanceledOnTouchOutside(true);
        getWindow().getAttributes().width = -2;
        getWindow().getAttributes().height = -2;
        getWindow().getAttributes().y = 0;
        getWindow().setGravity(Gravity.CENTER_VERTICAL);
        getWindow().setAttributes(getWindow().getAttributes());
        if (context instanceof Activity)
            setOwnerActivity((Activity) context);
        this.context = context;
        this.order = order;

        setContentView(R.layout.dialog_order_action);
        tv_cuidan = (TextView) findViewById(R.id.tv_cuidan);
        tv_print = (TextView) findViewById(R.id.tv_print);
        tv_hang = (TextView) findViewById(R.id.tv_hang);
    }


    public void setButton1(DialogButtonOnClickListener clickListener) {
        this.listener_1 = clickListener;
        this.tv_cuidan.setOnClickListener(this);
    }


    public void setButton2(DialogButtonOnClickListener clickListener) {
        this.listener_2 = clickListener;
        this.tv_print.setOnClickListener(this);
    }

    public void setButton3(DialogButtonOnClickListener clickListener) {
        this.listener_3 = clickListener;
        this.tv_hang.setOnClickListener(this);
    }

    public static interface DialogButtonOnClickListener {
        public void onClick(View button, OrderActionDialog dialog, Order order);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cuidan) {
            if (listener_1 != null) {
                listener_1.onClick(v, this, order);
            }
        } else if (id == R.id.tv_print) {
            if (listener_2 != null) {
                listener_2.onClick(v, this, order);
            }
        } else if (id == R.id.tv_hang) {
            if (listener_3 != null) {
                listener_3.onClick(v, this, order);
            }
        }
    }
}
