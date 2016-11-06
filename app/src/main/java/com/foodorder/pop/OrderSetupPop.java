package com.foodorder.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.activity.GoodListActivity;
import com.foodorder.contant.AppKey;
import com.foodorder.contant.EventTag;
import com.foodorder.dialog.LoadingDialog;
import com.foodorder.dialog.NormalDialog;
import com.foodorder.logic.CartManager;
import com.foodorder.runtime.ActivityManager;
import com.foodorder.runtime.RT;
import com.foodorder.runtime.WeakHandler;
import com.foodorder.runtime.event.EventManager;
import com.foodorder.server.api.API_Food;
import com.foodorder.server.callback.JsonResponseCallback;
import com.foodorder.util.SoftKeyboardUtil;
import com.foodorder.util.ToastUtil;

import org.json.JSONObject;

import static com.foodorder.runtime.RT.getString;

/**
 * Created by guodong on 2016/5/31 12:05.
 */
public class OrderSetupPop extends PopupWindow implements View.OnClickListener {

    private Context mContext;
    private ImageButton ib_close;
    private EditText et_num;
    private TextView tv_minus, tv_count, tv_add;
    private LinearLayout ll_number;
    private Button btn_ok;
    private String id_order;
    private int count = 1;

    public OrderSetupPop(Context context, String id_order) {
        this.mContext = context;
        this.id_order = id_order;
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
        ll_number = (LinearLayout) view.findViewById(R.id.ll_number);

        et_num.setTransformationMethod(new AllCapTransformationMethod());


        ib_close.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        tv_minus.setOnClickListener(this);
        tv_add.setOnClickListener(this);

        if (CartManager.ins().isPack) {
            ll_number.setVisibility(View.GONE);
        } else {
            ll_number.setVisibility(View.VISIBLE);
            et_num.requestFocus();
            new WeakHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SoftKeyboardUtil.showSoftKeyboard(et_num);
                }
            }, 500);

        }
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
                String number = et_num.getText().toString().trim().toUpperCase();
                if (!CartManager.ins().isPack && TextUtils.isEmpty(number)) {
                    ToastUtil.showToast(getString(R.string.good_taihao_empty));
                    return;
                }

                SoftKeyboardUtil.hideSoftKeyboard(et_num);
                String persons = tv_count.getText().toString().trim();
//                DLOG.json(CartManager.ins().getOrderGoodJson(false, id_order, number, persons));
                showOrderGoodDialog(mContext, id_order, number, persons);
//                API_Food.ins().orderGood(AppKey.HTTP_TAG, CartManager.ins().getOrderGoodJson(CartManager.ins().isPack, id_order, number, persons), new JsonResponseCallback() {
//                    @Override
//                    public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
//                        if (errcode == 200) {
//                            CartManager.ins().clear();
//                            EventManager.ins().sendEvent(EventTag.GOOD_LIST_REFRESH, 0, 0, true);
//                            EventManager.ins().sendEvent(EventTag.GOOD_SEARCH_LIST_REFRESH, 0, 0, null);
//                            EventManager.ins().sendEvent(EventTag.ORDER_LIST_REFRESH, 0, 0, null);
//                        }
//                        ToastUtil.showToast(errmsg);
//                        return false;
//                    }
//                });
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

    public void showOrderGoodDialog(Context context, final String id_order, final String number, final String persons) {
        NormalDialog dialog = new NormalDialog(context);
        dialog.setTitle(R.string.good_order_dialog_title);
        dialog.setTextDes(getString(R.string.good_order_dialog_desc));
        dialog.setButton1(getString(R.string.action_cancel), new NormalDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, NormalDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.setButton2(getString(R.string.action_ok), new NormalDialog.DialogButtonOnClickListener() {
            @Override
            public void onClick(View button, NormalDialog dialog) {
                dialog.dismiss();
                showLoadingDialog(false);
                API_Food.ins().orderGood(AppKey.HTTP_TAG, CartManager.ins().getOrderGoodJson(CartManager.ins().isPack, id_order, number, persons), new JsonResponseCallback() {
                    @Override
                    public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
                        hideLoadingDialog();
                        if (errcode == 200) {
                            CartManager.ins().clear();
                            EventManager.ins().sendEvent(EventTag.GOOD_LIST_REFRESH, 0, 0, true);
                            EventManager.ins().sendEvent(EventTag.GOOD_SEARCH_LIST_REFRESH, 0, 0, null);
                            EventManager.ins().sendEvent(EventTag.ORDER_LIST_REFRESH, 0, 0, null);
                            ToastUtil.showToast(RT.getString(R.string.good_order_success));
//                            Intent intent = new Intent(mContext, OrdersActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            mContext.startActivity(intent);
                            ActivityManager.ins().finishActivity(GoodListActivity.class);
                            ((Activity) mContext).finish();
                        } else {
                            ToastUtil.showToast(RT.getString(R.string.good_order_failed));
                        }
                        return false;
                    }
                });
            }
        });
        dialog.show();
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

    private LoadingDialog mLoadingDialog;

    protected void showLoadingDialog(boolean canCancel) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext);
        }
        mLoadingDialog.setCancelable(canCancel);
        mLoadingDialog.show();
    }

    protected void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
}
