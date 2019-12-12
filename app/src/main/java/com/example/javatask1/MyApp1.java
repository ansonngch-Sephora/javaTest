package com.example.javatask1;

import android.app.Application;

import com.dynamicyield.dyapi.DYApi;

public class MyApp1 extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DYApi.setContextAndSecret(getApplicationContext(),"a1cd47fdecbed0cd3806399d");
        DYApi.getInstance().enableDeveloperLogs(true);

    }
}
