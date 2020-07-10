package org.ar.rtm;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatManager {
    private static final String TAG = ChatManager.class.getSimpleName();

    private Context mContext;
    private RtmClient mRtmClient;
    private SendMessageOptions mSendMsgOptions;
    private List<RtmClientListener> mListenerList = new ArrayList<>();

    public ChatManager(Context context) {
        mContext = context;
    }

    public void init() {
        String appID = "";

        try {
            mRtmClient = RtmClient.createInstance(mContext, appID, new RtmClientListener() {
                @Override
                public void onConnectionStateChanged(int state, int reason) {
                    for (RtmClientListener listener : mListenerList) {
                        listener.onConnectionStateChanged(state, reason);
                    }

                    Log.d("zhuangtai","state="+state+"==reason"+reason);
                }

                @Override
                public void onMessageReceived(RtmMessage rtmMessage, String peerId) {
                    if (mListenerList.isEmpty()) {
                    } else {
                        for (RtmClientListener listener : mListenerList) {
                            listener.onMessageReceived(rtmMessage, peerId);
                        }
                    }
                }




                @Override
                public void onTokenExpired() {

                }

                @Override
                public void onPeersOnlineStatusChanged(Map<String, Integer> status) {
                    if (mListenerList.isEmpty()) {
                    } else {
                        for (RtmClientListener listener : mListenerList) {
                            listener.onPeersOnlineStatusChanged(status);
                        }
                    }
                }
            });


        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtm sdk init fatal error\n" + Log.getStackTraceString(e));
        }

        // Global option, mainly used to determine whether
        // to support offline messages now.
        mSendMsgOptions = new SendMessageOptions();
    }

    public RtmClient getRtmClient() {
        return mRtmClient;
    }

    public void registerListener(RtmClientListener listener) {
        mListenerList.add(listener);
    }

    public void unregisterListener(RtmClientListener listener) {
        mListenerList.remove(listener);
    }

    public void enableOfflineMessage(boolean enabled) {
        mSendMsgOptions.enableOfflineMessaging = enabled;
    }

    public boolean isOfflineMessageEnabled() {
        return mSendMsgOptions.enableOfflineMessaging;
    }

    public SendMessageOptions getSendMessageOptions() {
        return mSendMsgOptions;
    }

}
