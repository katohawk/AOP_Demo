package com.netease.aop.login;

import android.app.Application;

/**
 * Created by hk on 2020/5/6.
 */
public class MyApplication extends Application {

    private static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static Application getApplication(){
        return sApplication;
    }
}
