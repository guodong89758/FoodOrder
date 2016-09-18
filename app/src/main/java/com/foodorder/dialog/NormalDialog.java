package com.foodorder.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.foodorder.R;


public class NormalDialog extends Dialog implements View.OnClickListener {

    private DialogButtonOnClickListener listener_1, listener_2;
    private Context context;
    private TextView tv_des;
    private View line_1;
    private Button btn_1;
    private View line_2;
    private Button btn_2;
    private TextView tvPromptTitle;

    public NormalDialog(Context context) {
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

        setContentView(R.layout.dialog_normal);
        tv_des = (TextView) findViewById(R.id.tv_des);
        tvPromptTitle = (TextView) findViewById(R.id.tvPromptTitle);
        line_1 = findViewById(R.id.line_1);
        btn_1 = (Button) findViewById(R.id.btn_1);
        line_2 = findViewById(R.id.line_2);
        btn_2 = (Button) findViewById(R.id.btn_2);
        tvPromptTitle.setVisibility(View.GONE);
    }

    public void setTextDes(String text) {
        tv_des.setText(text);
        tv_des.setTextColor(context.getResources().getColor(R.color.black_90));
    }

    public void setTextDesColor(int textColor) {
        tv_des.setTextColor(textColor);
    }

    public void setButton1(String text, DialogButtonOnClickListener clickListener) {
        this.btn_1.setText(text);
        this.btn_1.setVisibility(View.VISIBLE);
        this.listener_1 = clickListener;
        this.btn_1.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        fixlayout();
    }

    public void setNoPomptTitle() {
        tvPromptTitle.setVisibility(View.INVISIBLE);
    }

    public void setPromptTitle(String promptTitle) {
        if (!TextUtils.isEmpty(promptTitle)) {
            tvPromptTitle.setText(promptTitle);
        } else {
            tvPromptTitle.setText("提示");
        }
    }

    public void setButton2(String text, DialogButtonOnClickListener clickListener) {
        this.btn_2.setText(text);
        this.btn_2.setVisibility(View.VISIBLE);
        this.listener_2 = clickListener;
        this.btn_2.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        fixlayout();
    }

    private void fixlayout() {
        if (btn_1.getVisibility() == View.VISIBLE && btn_2.getVisibility() == View.GONE) {
            // 显示btn1 隐藏btn2
            line_1.setVisibility(View.VISIBLE);
            line_2.setVisibility(View.GONE);

        } else if (btn_1.getVisibility() == View.GONE && btn_2.getVisibility() == View.VISIBLE) {
            // 显示btn2 隐藏btn1
            line_1.setVisibility(View.VISIBLE);
            line_2.setVisibility(View.GONE);

        } else if (btn_1.getVisibility() == View.VISIBLE && btn_2.getVisibility() == View.VISIBLE) {
            // 两个btn都显示
            line_1.setVisibility(View.VISIBLE);
            line_2.setVisibility(View.VISIBLE);

        } else {
            // 都隐藏
            line_1.setVisibility(View.GONE);
            line_2.setVisibility(View.GONE);
            btn_1.setVisibility(View.GONE);
            btn_2.setVisibility(View.GONE);
        }
    }

    public static interface DialogButtonOnClickListener {
        public void onClick(View button, NormalDialog dialog);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_1) {
            if (listener_1 != null) {
                listener_1.onClick(v, this);
            }
        } else if (id == R.id.btn_2) {
            if (listener_2 != null) {
                listener_2.onClick(v, this);
            }
        }
    }
}
