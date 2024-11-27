package com.example.bicycles.Token;

import android.app.Application;
import android.content.Context;

import com.example.bicycles.Singleton.RetrofitClient;

public class MyAplication extends Application {
    private static MyAplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
