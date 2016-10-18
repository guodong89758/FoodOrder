package com.foodorder.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ReplacementTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.log.DLOG;
import com.foodorder.logic.CartManager;
import com.foodorder.util.SoftKeyboardUtil;

/**
 * Created by guodong on 2016/5/31 12:05.
 */
public class OrderSetupPop extends PopupWindow implements View.OnClickListener {

    private Context mContext;
    private ImageButton ib_close;
    private EditText et_num;
    private TextView tv_minus, tv_count, tv_add;
    private Button btn_ok;
    private int count = 0;

    public OrderSetupPop(Context context) {
        this.mContext = context;
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        setAnimationStyle(R.style.popupwindow_anim_style);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View view = LayoutInflater.from(context).inflate(R.layout.pop_setup_order, null);
        setContentView(view);
        initView(view);
        initData();
    }

    private void initView(View view) {
        ib_close = (ImageButton) view.findViewById(R.id.ib_close);
        et_num = (EditText) view.findViewById(R.id.et_num);
        tv_minus = (TextView) view.findViewById(R.id.tv_minus);
        tv_count = (TextView) view.findViewById(R.id.tv_count);
        tv_add = (TextView) view.findViewById(R.id.tv_add);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);

        et_num.setTransformationMethod(new AllCapTransformationMethod());


        ib_close.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        tv_minus.setOnClickListener(this);
        tv_add.setOnClickListener(this);
    }

    private void initData() {
        tv_count.setText(count + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_close:
                dismiss();
                break;
            case R.id.btn_ok:
                SoftKeyboardUtil.hideSoftKeyboard(et_num);
                DLOG.json(CartManager.ins().getOrderGoodJson());
                dismiss();
                break;
            case R.id.tv_add:
                count++;
                tv_count.setText(count + "");
                break;
            case R.id.tv_minus:
                if (count > 0) {
                    count--;
                    tv_count.setText(count + "");
                }
                break;
        }
    }

    @Override
    public void dismiss() {
        SoftKeyboardUtil.hideSoftKeyboard(et_num);
        super.dismiss();
        backgroundAlpha(1f);
    }

    public void showPopup() {
        this.showAtLocation(((Activity) mContext).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.5f);
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    public class AllCapTransformationMethod extends ReplacementTransformationMethod {

        @Override
        protected char[] getOriginal() {
            char[] aa = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            return aa;
        }

        @Override
        protected char[] getReplacement() {
            char[] cc = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            return cc;
        }

    }

}
