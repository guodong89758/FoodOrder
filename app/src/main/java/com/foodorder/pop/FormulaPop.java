package com.foodorder.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.adapter.FormulaAdapter;
import com.foodorder.contant.EventTag;
import com.foodorder.db.bean.Good;
import com.foodorder.logic.CartManager;
import com.foodorder.runtime.RT;
import com.foodorder.runtime.event.EventManager;
import com.foodorder.util.PhoneUtil;

/**
 * Created by guodong on 2016/5/31 12:05.
 */
public class FormulaPop extends PopupWindow implements View.OnClickListener {
    public static final int TYPE_MENU = 1; //菜单中使用
    public static final int TYPE_ORDER = 2; //订单详情中菜品列表中使用
    public static final int TYPE_UPDATE = 3; //购物车中修改使用
    private Context mContext;
    private TextView tv_name;
    private ImageButton ib_close;
    private Button btn_ok;
    private ListView lv_formula;
    private FormulaAdapter formulaAdapter;
    private Good good;
    private int type = TYPE_MENU;

    public FormulaPop(Context context, Good good, int type) {
        this.mContext = context;
        this.good = good;
        this.type = type;
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(RT.getScreenHeight() * 3 / 5);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        setAnimationStyle(R.style.popupwindow_anim_style);
        View view = LayoutInflater.from(context).inflate(R.layout.pop_formula_view, null);
        setContentView(view);
        initView(view);
        initData();
    }

    private void initView(View view) {
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        ib_close = (ImageButton) view.findViewById(R.id.ib_close);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);
        lv_formula = (ListView) view.findViewById(R.id.lv_formula);

        ib_close.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    private void initData() {
        if (good == null) {
            return;
        }
        String good_name = "";
        if (PhoneUtil.isZh()) {
            good_name = good.getZh_name();
        } else {
            good_name = good.getFr_name();
        }
        tv_name.setText(good_name);
        formulaAdapter = new FormulaAdapter();
        formulaAdapter.setGood(good);
        formulaAdapter.setType(type);
        formulaAdapter.setData(good.getFormulaList());
        lv_formula.setAdapter(formulaAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_close:
                dismiss();
                break;
            case R.id.btn_ok:
                if (type == TYPE_ORDER) {
                    dismiss();
                    return;
                }
                if (good != null && good.getAttributeList() != null && good.getAttributeList().size() > 0) {
                    EventManager.ins().sendEvent(EventTag.POPUP_ATTRIBUTE_SHOW, 0, type, good);
                } else {
                    if (type == TYPE_MENU) {
                        CartManager.ins().add(good, true);
                    }
                }
                dismiss();
                break;
        }
    }

    @Override
    public void dismiss() {
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
}
