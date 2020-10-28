package com.mvvm.dzk.base;

import android.app.Application;
import android.content.Context;

import com.kingja.loadsir.core.LoadSir;
import com.mvvm.dzk.base.loadsir.CustomCallback;
import com.mvvm.dzk.base.loadsir.EmptyCallback;
import com.mvvm.dzk.base.loadsir.ErrorCallback;
import com.mvvm.dzk.base.loadsir.LoadingCallback;
import com.mvvm.dzk.base.loadsir.TimeoutCallback;

public class BaseApplication extends Application {
    public static Context sApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
    }
}
