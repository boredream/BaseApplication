package com.boredream.baseapplication.base;


import android.app.Application;

import com.boredream.baseapplication.utils.AppKeeper;


public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppKeeper.init(this);
    }
}
