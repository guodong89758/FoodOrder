package com.foodorder.base;

import android.app.Application;

import com.foodorder.runtime.RT;
import com.karumi.dexter.Dexter;

/**
 * Created by guodong on 2016/6/28 18:44.
 */
public class FOApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RT.application = this;
        RT.ins().init();
        Dexter.initialize(this);
    }
}
