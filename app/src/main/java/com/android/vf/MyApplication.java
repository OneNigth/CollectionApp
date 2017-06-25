package com.android.vf;

import android.app.Application;
import android.util.Log;

/**
 * Created by yj on 2017/6/21.
 */

public class MyApplication extends Application {
    private static MyApplication context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static MyApplication getContext() {
        Log.d("当前上下文:",context+"");
        return context;
    }

}
