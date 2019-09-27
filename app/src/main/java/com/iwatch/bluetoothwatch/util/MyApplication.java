package com.iwatch.bluetoothwatch.util;

import android.app.Application;

/**
 * Created by DAIKO T540P on 2018/11/2.
 */

public class MyApplication extends Application {
    private static MyApplication _instance = null;
    @Override
    public void onCreate() {
        _instance = this;
        super.onCreate();
    }
    public static MyApplication getInstance(){
        return _instance;
    }
}
