package com.example.bicycles.Token;

import android.app.Application;

public class MyAplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        SharedPreferencesManager.getInstance(this);
    }
}
