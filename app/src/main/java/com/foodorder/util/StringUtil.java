package com.foodorder.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.foodorder.log.DLOG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by guodong on 16/9/18.
 */
public class StringUtil {

    /**
     * 从asset路径下读取对应文件转String输出
     *
     * @param mContext
     * @return
     */
    public static String getJson(Context mContext, String fileName) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }

    public static boolean checkTime() {
        long mostTime = 0;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s");
            Date date = dateFormat.parse("2016-11-1 0:0:0");
            mostTime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long nowTime = System.currentTimeMillis();
        DLOG.e("most time = " + mostTime);
        DLOG.e("now time = " + nowTime);
        if (nowTime < mostTime) {
            return true;
        } else {
            return false;
        }
    }
}
