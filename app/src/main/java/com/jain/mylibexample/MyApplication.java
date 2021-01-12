package com.jain.mylibexample;

import android.app.Application;

import com.jain.mylibrary.MySDK;

public class MyApplication extends Application {
    private static volatile MyApplication _instance;

    private MySDK mySDK;

    public static synchronized MyApplication getInstance() {
        if (_instance == null) {
            throw new UnsupportedOperationException("u can't instantiate me...");
        }
        return _instance;
    }

    public MySDK getSDKInstance() {
        return mySDK;
    }

    @Override
    public void onCreate() {
//        _instance = (MyApplication) getApplicationContext();
        super.onCreate();
        _instance = (MyApplication) getApplicationContext();

//        instance= (MyApplication) getApplicationContext();
        mySDK = MySDK.getInstance(_instance);
        mySDK.init();
    }

}