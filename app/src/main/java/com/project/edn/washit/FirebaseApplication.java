package com.project.edn.washit;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by EDN on 5/29/2017.
 */

public class FirebaseApplication extends Application {
    private static boolean IsMainActivityOpen = false;
    private static FirebaseApplication mInstance=null;



    public boolean IsMainActivityOpen() {
        return IsMainActivityOpen;
    }

    public  void setMaintActivityOpen(boolean isMainActivityOpen) {
        FirebaseApplication.IsMainActivityOpen = isMainActivityOpen;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public static synchronized FirebaseApplication getInstance() {
        if (mInstance==null){
            mInstance=new FirebaseApplication();
        }
        return mInstance;
    }
}
