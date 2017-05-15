package com.foodorder.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.foodorder.runtime.RT;

import java.util.Locale;

/**
 * Created by guodong on 16/9/18.
 */
public class PhoneUtil {

    public static int dipToPixel(float dp, Context mContext) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        int pixel = (int) (dp * scale + 0.5f);
        return pixel;
    }

    public static float pixelToDip(int pixel, Context mContext) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        float dp = (float) (pixel - 0.5f) / scale;
        return dp;
    }

    public static boolean isZh() {
        Locale locale = RT.application.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    /**
     * 判断是否大于6英寸
     *
     * @return
     */
    public static boolean isMoreThan6Inch(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        // 屏幕宽度
        float screenWidth = display.getWidth();
        // 屏幕高度
        float screenHeight = display.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        // 大于6尺寸则为Pad
        if (screenInches >= 6.0) {
            return true;
        }
        return false;
    }

    public static boolean hasPhone(Activity activity) {
        TelephonyManager telephony = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPad(Activity activity) {
        return isMoreThan6Inch(activity) && isScreenSizeLarge(activity);
    }

    /**
     * 判断设备是否为大尺寸屏幕
     *
     * @param context
     * @return
     */
    public static boolean isScreenSizeLarge(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
