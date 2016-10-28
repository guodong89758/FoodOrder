package com.foodorder.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.foodorder.R;
import com.foodorder.logic.UserManager;
import com.foodorder.runtime.RT;
import com.foodorder.util.PhoneUtil;

/**
 * Created by guodong on 2016/5/31 12:05.
 */
public class LoginUserPop extends PopupWindow implements View.OnClickListener {

    private Context mContext;
    private ListView lv_user;

    public LoginUserPop(Context context) {
        this.mContext = context;
        setWidth(RT.getScreenWidth() - PhoneUtil.dipToPixel(40, context));
        if (UserManager.getInstance().getUserList().size() > 5) {
            setHeight(PhoneUtil.dipToPixel(250, context));
        } else {
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        setAnimationStyle(R.style.login_user_anim_style);
        View view = LayoutInflater.from(context).inflate(R.layout.pop_login_user, null);
        setContentView(view);
        initView(view);
        initData();
    }

    private void initView(View view) {
        lv_user = (ListView) view.findViewById(R.id.lv_user);
    }

    private void initData() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, UserManager.getInstance().getUserList());
        lv_user.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void dismiss() {
        super.dismiss();
//        backgroundAlpha(1f);
    }

    public void showPopup(View view) {
//        this.showAtLocation(((Activity) mContext).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
//        backgroundAlpha(0.5f);
        this.showAsDropDown(view);
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
