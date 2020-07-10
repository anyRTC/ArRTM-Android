package org.ar.rtm;

import android.app.Application;

public class ARApplication extends Application {

    private static ARApplication sInstance;

    private ChatManager mChatManager;
    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefUtil.init(this);
        sInstance =this;
        mChatManager = new ChatManager(this);
        mChatManager.init();
    }

    public static ARApplication the() {
        return sInstance;
    }
    public ChatManager getChatManager() {
        return mChatManager;
    }

}
