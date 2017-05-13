package com.foodorder.util;

import android.content.Context;
import android.content.res.Configuration;

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
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
