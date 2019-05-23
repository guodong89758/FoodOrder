package com.foodorder.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.util.SoftKeyboardUtil;
import com.foodorder.util.ToastUtil;

import static com.foodorder.runtime.RT.getString;


public class SetupNumDialog extends Dialog implements View.OnClickListener {

    private OnSetupNumListener numListener;
    private Context context;
    private TextView tv_name;
    private EditText et_num;
    private Button btn_ok, btn_cancel;

    public SetupNumDialog(Context context, int num) {
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

        setContentView(R.layout.dialog_setup_num);
        tv_name = findViewById(R.id.tv_name);
        et_num = findViewById(R.id.et_num);
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        if (num > 0) {
            et_num.setText(num + "");
            et_num.setSelection(String.valueOf(num).length());
        }
    }


    public interface OnSetupNumListener {
        void onGetNum(int num);
    }

    public void setOnSetupNumListener(OnSetupNumListener listener) {
        this.numListener = listener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_cancel) {
            SoftKeyboardUtil.hideSoftKeyboard(et_num);
            this.dismiss();
        } else if (id == R.id.btn_ok) {
            String num = et_num.getText().toString();
            if (TextUtils.isEmpty(num)) {
                ToastUtil.showToast(getString(R.string.dialog_good_num_empty));
                return;
            }
            if (numListener != null) {
                numListener.onGetNum(Integer.parseInt(num));
            }
            SoftKeyboardUtil.hideSoftKeyboard(et_num);
            this.dismiss();
        }
    }

}
