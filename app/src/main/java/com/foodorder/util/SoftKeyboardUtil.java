package com.foodorder.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.foodorder.runtime.RT;


/**
 * Created by guodong on 2016/3/18 15:28.
 */
public class SoftKeyboardUtil {

    public static void showSoftKeyboard(View view) {
        if (view == null) {
            return;
        }
        ((InputMethodManager) RT.application.getSystemService(
                Context.INPUT_METHOD_SERVICE)).showSoftInput(view,
                InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftKeyboard(View view) {
        if (view == null)
            return;
        ((InputMethodManager) RT.application.getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                view.getWindowToken(), 0);
    }

    public static void toogleSoftKeyboard() {
        ((InputMethodManager) RT.application.getSystemService(
                Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

//    public static boolean isActive() {
//        return ((InputMethodManager) RT.application.getSystemService(
//                Context.INPUT_METHOD_SERVICE)).isActive();
//    }
}
