package com.daylab.g2048;

import com.activeandroid.ActiveAndroid;

import android.app.Application;

public class MyApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
    
    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
