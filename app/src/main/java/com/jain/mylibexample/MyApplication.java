package com.jain.mylibexample;

import android.app.Application;

import com.jain.mylibrary.MySDK;

public class MyApplication extends Application {
    private static volatile MyApplication _instance;

    private MySDK cleverTap;

    public static synchronized MyApplication getInstance() {
        if (_instance == null) {
            throw new UnsupportedOperationException("u can't instantiate me...");
        }
        return _instance;
    }

    public MySDK getCleverTapInstance() {
        return cleverTap;
    }

    @Override
    public void onCreate() {
//        _instance = (MyApplication) getApplicationContext();
        super.onCreate();
        _instance = (MyApplication) getApplicationContext();

//        instance= (MyApplication) getApplicationContext();
        cleverTap = MySDK.getInstance(_instance);
        cleverTap.init();
    }

}