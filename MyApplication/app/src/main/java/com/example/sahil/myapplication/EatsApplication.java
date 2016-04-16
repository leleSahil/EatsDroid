package com.example.sahil.myapplication;

import android.app.Application;

import feast.FeastAPI;

/**
 * Created by Riley on 4/13/16.
 */
public class EatsApplication extends Application {

    public void onCreate()
    {
        super.onCreate();

        FeastAPI.sharedAPI.setContext(getApplicationContext());
    }
}
