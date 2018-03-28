package com.example.bianlifeng.coolweather;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by bianlifeng on 2018/3/28.
 */

public class MyApplication extends Application {

    public static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        LitePal.initialize(context);
    }
    public static Context getContext(){
        return context;
    }

}
