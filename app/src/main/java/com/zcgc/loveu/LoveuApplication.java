package com.zcgc.loveu;

import android.app.Application;

import com.zcgc.loveu.manager.UserManager;

public class LoveuApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // User实体
        UserManager.getInstance();
    }
}
