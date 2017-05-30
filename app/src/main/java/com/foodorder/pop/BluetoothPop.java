package com.foodorder.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.foodorder.R;
import com.foodorder.adapter.ToothAdapter;
import com.foodorder.entry.Bluetooth;
import com.foodorder.logic.UserManager;
import com.foodorder.runtime.RT;
import com.foodorder.util.PhoneUtil;

import java.util.List;

/**
 * Created by guodong on 2016/5/31 12:05.
 */
public class BluetoothPop extends PopupWindow {

    private Context mContext;
    private ListView lv_bluetooth;
    private ToothAdapter toothAdapter;
    private OnBluetoothSelectedListener listener;

    public BluetoothPop(Context context) {
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
//        setAnimationStyle(R.style.login_user_anim_style);
        View view = LayoutInflater.from(context).inflate(R.layout.pop_bluetooth, null);
        setContentView(view);
        initView(view);
        initData();
    }

    private void initView(View view) {
        lv_bluetooth = (ListView) view.findViewById(R.id.lv_bluetooth);
    }

    private void initData() {
        toothAdapter = new ToothAdapter();
        lv_bluetooth.setAdapter(toothAdapter);

        lv_bluetooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.selectedBluetooth((Bluetooth) toothAdapter.getItem(position));
                }
                dismiss();
            }
        });
    }

    public void setData(List<Bluetooth> data) {
        if (data == null) {
            return;
        }
        toothAdapter.setData(data);
        toothAdapter.notifyDataSetChanged();
    }


    @Override
    public void dismiss() {
        super.dismiss();
//        backgroundAlpha(1f);
    }

    public void showPopup(View view) {
//        this.showAtLocation(((Activity) mContext).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
//        backgroundAlpha(0.5f);
        this.showAsDropDown(view, 0, PhoneUtil.dipToPixel(-8, mContext));
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

    public interface OnBluetoothSelectedListener {
        void selectedBluetooth(Bluetooth bluetooth);
    }

    public void setOnBluetoothSelectedListener(OnBluetoothSelectedListener listener) {
        this.listener = listener;
    }
}
