package com.foodorder.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.foodorder.R;


public class LoadingDialog extends Dialog {
    private TextView tv_text;

    public LoadingDialog(Context context) {
        super(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress_layout, null);
        tv_text = (TextView) view.findViewById(R.id.tv_text);
        setContentView(view);
    }

    public void setContent(String content) {
        tv_text.setText(content);
    }


    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
