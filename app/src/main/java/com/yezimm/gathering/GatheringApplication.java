package com.yezimm.gathering;

import android.app.Application;

/**
 * Created by admin on 2015/11/10.
 */
public class GatheringApplication extends Application {

    public static GatheringApplication instance ;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this ;
    }

}
