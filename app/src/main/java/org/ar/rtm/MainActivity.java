package org.ar.rtm;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RtmClient mRtmClient;
    private RecyclerView rvList;
    private EditText editText;
    private MessageApdater messageApdater;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLogin =false;
    private RtmChannel rtmChannel;
    private int type = 1;
    private String id="";
    private CardView query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        type = getIntent().getIntExtra("type",1);
        id = getIntent().getStringExtra("id");
        query=findViewById(R.id.query);
        query.setVisibility(type==1?View.GONE:View.VISIBLE);
        setTitle(type == 1 ? "单聊("+id+")":"群聊("+id+")");
        rvList=findViewById(R.id.rl_list);
        editText =findViewById(R.id.et_content);
        mRtmClient = ARApplication.the().getChatManager().getRtmClient();

        ARApplication.the().getChatManager().registerListener(rtmClientListener);
        messageApdater = new MessageApdater();
         linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.scrollToPositionWithOffset(messageApdater.getItemCount() - 1, Integer.MIN_VALUE);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setAdapter(messageApdater);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEND){
                    if (type==1){
                        sendText();
                    }else {
                        sendChannelText();
                    }
                return true;
                }

                return false;
            }
        });

        if (type==1){

        }else {
            joinChannel();
        }

    }


    RtmClientListener rtmClientListener =new RtmClientListener() {
        @Override
        public void onConnectionStateChanged(int var1, int var2) {

        }

        @Override
        public void onMessageReceived(final RtmMessage var1, final String var2) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (type==1) {
                        if (!var2.equals(id)) {
                            addData(new MessageBean("", "收到来自" + var2 + "的消息:" + var1.getText(), false));
                        } else {
                            addData(new MessageBean("", var2 + ":" + var1.getText(), false));
                        }
                    }

                }
            });
        }

        @Override
        public void onTokenExpired() {

        }

        @Override
        public void onPeersOnlineStatusChanged(Map<String, Integer> var1) {

        }


    };

    public void joinChannel(){
        rtmChannel=mRtmClient.createChannel(id, new RtmChannelListener() {
            @Override
            public void onMemberCountUpdated(final int var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","频道人员更新  "+var1,false));

                    }
                });
            }

            @Override
            public void onAttributesUpdated(final List<RtmChannelAttribute> var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","有人更新了频道属性，如下",false));
                        for (int i=0;i<var1.size();i++){
                            addData(new MessageBean("",var1.get(i).getLastUpdateUserId()+"\nKEY="+var1.get(i).getKey()+"\nValue="+var1.get(i).getValue(),false));
                        }
                    }
                });
            }

            @Override
            public void onMessageReceived(final RtmMessage var1, final RtmChannelMember var2) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (type==2) {
                            addData(new MessageBean("", var2.getUserId() + ":" + var1.getText(), false));
                        }
                    }
                });
            }

            @Override
            public void onMemberJoined(final RtmChannelMember var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        addData(new MessageBean("",var1.getUserId()+"加入了频道～",false));
                    }
                });
            }

            @Override
            public void onMemberLeft(final RtmChannelMember var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        addData(new MessageBean("",var1.getUserId()+"离开了频道～",false));
                    }
                });
            }
        });

        rtmChannel.join(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","加入频道成功",true));
                    }
                });
            }

            @Override
            public void onFailure(ErrorInfo var1) {

            }
        });

    }





    public void sendText(){
        if (mRtmClient==null){
            return;
        }
        if (!TextUtils.isEmpty(editText.getText().toString())){

            final String content = editText.getText().toString();
            SendMessageOptions options=new SendMessageOptions();
            options.enableHistoricalMessaging=false;
            options.enableOfflineMessaging=false;
                mRtmClient.sendMessageToPeer(id, mRtmClient.createMessage(content), options, new ResultCallback<Void>() {
                    @Override
                    public void onSuccess(Void var1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addData(new MessageBean("",content,true));
                                editText.setText("");
                            }
                        });

                    }

                    @Override
                    public void onFailure(final ErrorInfo var1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addData(new MessageBean("","给"+id+"发："+content+"失败了 原因"+var1.getErrorDescription(),true));
                            }
                        });
                    }
                });

        }

    }

    public void sendChannelText(){
        if (mRtmClient==null){
            return;
        }
        if (!TextUtils.isEmpty(editText.getText().toString())){

            final String content = editText.getText().toString();
                rtmChannel.sendMessage( mRtmClient.createMessage(content), null, new ResultCallback<Void>() {
                    @Override
                    public void onSuccess(Void var1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addData(new MessageBean("",content,true));
                                editText.setText("");
                            }
                        });

                    }

                    @Override
                    public void onFailure(ErrorInfo var1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addData(new MessageBean("", "发：" + content + "失败了", true));
                            }
                        });
                    }
                });
        }

    }

    public void addData(MessageBean bean){
        messageApdater.addData(bean);
        linearLayoutManager.scrollToPositionWithOffset(messageApdater.getItemCount() - 1, Integer.MIN_VALUE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (type==1){//单聊
            }else {
                rtmChannel.leave(new ResultCallback<Void>() {
                    @Override
                    public void onSuccess(Void var1) {
                    }

                    @Override
                    public void onFailure(ErrorInfo var1) {

                    }
                });
                rtmChannel=null;
            }
            ARApplication.the().getChatManager().unregisterListener(rtmClientListener);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void get_members(View view) {
        if (type!=1){
            if (rtmChannel!=null){
                rtmChannel.getMembers(new ResultCallback<List<RtmChannelMember>>() {
                    @Override
                    public void onSuccess(final List<RtmChannelMember> var1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addData(new MessageBean("","频道人数列表如下",true));
                                for (int i = 0;i<var1.size();i++){
                                    addData(new MessageBean("",var1.get(i).getUserId(),true));
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(ErrorInfo var1) {

                    }
                });
            }
        }
    }
}
