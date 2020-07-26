package org.ar.ar_rtm_tutorial_android;

import android.app.Application;

public class RTMApplication extends Application {

    private static RTMApplication instance;

    private RtmManager rtmManager;

    private String userId = "";


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

   public static RTMApplication getInstance(){
        return instance;
   }

   public RtmManager getRtmManager(){
        if (rtmManager == null){
            rtmManager = new RtmManager(getApplicationContext());
            rtmManager.init();
        }
        return rtmManager;
   }

   public void ReleaseRtmManager(){
       rtmManager.release();
       rtmManager = null;
   }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
