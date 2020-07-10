package org.ar.rtm;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class QueryActivity extends AppCompatActivity implements RtmClientListener {

    RtmClient rtmClient;
    EditText et_uesr;
    RecyclerView rvList;
    private MessageApdater messageApdater;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        setTitle("查询");
        et_uesr= findViewById(R.id.et_uesr);
        rvList =findViewById(R.id.rv_list);
        messageApdater = new MessageApdater();
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.scrollToPositionWithOffset(messageApdater.getItemCount() - 1, Integer.MIN_VALUE);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setAdapter(messageApdater);
        rtmClient = ARApplication.the().getChatManager().getRtmClient();
        ARApplication.the().getChatManager().registerListener(this);
    }

    public void chaxun(View view) {
        if (et_uesr.getText().toString().isEmpty()){
            return;
        }
        Set<String> queryList = new HashSet<>();
        queryList.add(et_uesr.getText().toString());
        rtmClient.queryPeersOnlineStatus(queryList, new ResultCallback<Map<String, Boolean>>() {
            @Override
            public void onSuccess(final Map<String, Boolean> var1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (var1.size()>0){
                                for(Map.Entry<String, Boolean> entry : var1.entrySet()){
                                    String mapKey = entry.getKey();
                                    boolean mapValue = entry.getValue();
                                   addData(new MessageBean("",mapKey+(mapValue?"在线":"不在线"),true));
                                }
                            }
                        }
                    });
            }

            @Override
            public void onFailure(ErrorInfo var1) {

            }
        });
    }

    public void dingyue(View view) {
        if (et_uesr.getText().toString().isEmpty()){
            return;
        }
        Set<String> subList = new HashSet<>();
        subList.add(et_uesr.getText().toString());
        rtmClient.subscribePeersOnlineStatus(subList, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void var1) {

            }

            @Override
            public void onFailure(ErrorInfo var1) {

            }
        });
       addData(new MessageBean("","订阅："+et_uesr.getText().toString(),true));
    }

    public void cancle_dingyue(View view) {
        if (et_uesr.getText().toString().isEmpty()){
            return;
        }
        Set<String> subList = new HashSet<>();
        subList.add(et_uesr.getText().toString());
        rtmClient.unsubscribePeersOnlineStatus(subList, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void var1) {

            }

            @Override
            public void onFailure(ErrorInfo var1) {

            }
        });
        addData(new MessageBean("","取消订阅："+et_uesr.getText().toString(),true));
    }

    public void query_channel_num(View view) {
        if (et_uesr.getText().toString().isEmpty()){
            return;
        }
        List<String> channelList = new ArrayList<>();
        channelList.add(et_uesr.getText().toString());
        rtmClient.getChannelMemberCount(channelList, new ResultCallback<List<RtmChannelMemberCount>>() {
            @Override
            public void onSuccess(final List<RtmChannelMemberCount> var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i =0;i<var1.size();i++){
                            addData(new MessageBean("","频道"+var1.get(i).getChannelID()+"人数："+var1.get(i).getMemberCount(),true));
                        }
                    }
                });

            }

            @Override
            public void onFailure(ErrorInfo var1) {

            }
        });
    }

    @Override
    public void onConnectionStateChanged(int var1, int var2) {

    }

    @Override
    public void onMessageReceived(RtmMessage var1, String var2) {

    }

    @Override
    public void onTokenExpired() {

    }

    @Override
    public void onPeersOnlineStatusChanged(final Map<String, Integer> var1) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(Map.Entry<String, Integer> entry : var1.entrySet()){
                    String mapKey = entry.getKey();
                    Integer mapValue = entry.getValue();
                    addData(new MessageBean("","订阅的人："+mapKey+"状态改变回调"+(mapValue==0?"在线":"不在线"),true));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ARApplication.the().getChatManager().unregisterListener(this);
    }

    public void addData(MessageBean bean){
        messageApdater.addData(bean);
        linearLayoutManager.scrollToPositionWithOffset(messageApdater.getItemCount() - 1, Integer.MIN_VALUE);
    }

    public void queryPeersBySub(View view) {
        rtmClient.queryPeersBySubscriptionOption(0, new ResultCallback<Set<String>>() {
            @Override
            public void onSuccess(final Set<String> var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("", "获取成功，如下", true));
                        if (var1.size() > 0) {
                            for (int i =0;i<var1.size();i++){
                                addData(new MessageBean("", var1.iterator().next(), true));
                            }

                        }
                    }
                });


                };


            @Override
            public void onFailure(ErrorInfo var1) {

            }
        });
    }
}