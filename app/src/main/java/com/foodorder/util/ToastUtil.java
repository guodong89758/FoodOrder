package com.foodorder.util;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.foodorder.R;
import com.foodorder.runtime.RT;


public class ToastUtil {

    private static Toast mToast;

    public static void showToast(String obj) {
        if (obj != null) {
            if (RT.application != null) {
                handler.obtainMessage(0, obj).sendToTarget();
            }
        }

    }

    public static void showBottomToast(String obj) {
        if (obj != null) {
            if (RT.application != null) {
                handler.obtainMessage(0, 1, 0, obj).sendToTarget();
            }
        }

    }

    static Handler handler = new Handler(RT.application.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (msg.arg1 == 0) {
                        if (mToast == null) {
                            mToast = new Toast(RT.application);
                            mToast.setDuration(Toast.LENGTH_SHORT);
//                            mToast.setGravity(Gravity.CENTER, 0, 0);
                            View view = LayoutInflater.from(RT.application).inflate(R.layout.toast_layout, null);
                            mToast.setView(view);
                        }
                        if (msg.obj != null) {
                            View views = mToast.getView();
                            TextView tv = (TextView) views.findViewById(R.id.toast);
                            tv.setText(msg.obj.toString());
                            if (!TextUtils.isEmpty(msg.obj.toString())) {
                                mToast.show();
                            }
                        }
                    } else {
                        Toast.makeText(RT.application, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }

        ;
    };

}
