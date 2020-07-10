package org.ar.rtm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CallManageActivity extends AppCompatActivity {

    RtmClient rtmClient;
    RtmCallManager rtmCallManager;
    LocalInvitation localInvitation;
    EditText et_uesr;
    RecyclerView rvList;
    private MessageApdater messageApdater;
    private LinearLayoutManager linearLayoutManager;
    private AlertDialog dialogCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_manage);
        setTitle("呼叫");
        et_uesr= findViewById(R.id.et_uesr);
        rvList =findViewById(R.id.rv_list);
        messageApdater = new MessageApdater();
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.scrollToPositionWithOffset(messageApdater.getItemCount() - 1, Integer.MIN_VALUE);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setAdapter(messageApdater);
        rtmClient = ARApplication.the().getChatManager().getRtmClient();
        rtmCallManager = rtmClient.getRtmCallManager();
        rtmCallManager.setEventListener(new RtmCallEventListener() {
            @Override
            public void onLocalInvitationReceivedByPeer(final LocalInvitation var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (var1.getState()==1){
                           addData(new MessageBean("","已发送邀请",true));
                        }else if (var1.getState()==2){
                           addData(new MessageBean("","对方已经收到邀请",true));

                        }
                    }
                });
            }

            @Override
            public void onLocalInvitationAccepted(final LocalInvitation var1, String var2) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("",var1.getCalleeId()+"同意了你的呼叫",true));
                    }
                });
            }

            @Override
            public void onLocalInvitationRefused(final LocalInvitation var1, String var2) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("",var1.getCalleeId()+"拒绝了你的呼叫",true));
                    }
                });
            }

            @Override
            public void onLocalInvitationCanceled(final LocalInvitation var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","你取消了呼叫",true));
                    }
                });
            }

            @Override
            public void onLocalInvitationFailure(LocalInvitation var1, int var2) {

            }

            @Override
            public void onRemoteInvitationReceived(final RemoteInvitation var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","收到来自"+var1.getCallerId()+"的呼叫",true));
                        showCallDialog(var1);

                    }
                });
                Log.d("收到呼叫",var1.getCallerId());
            }

            @Override
            public void onRemoteInvitationAccepted(final RemoteInvitation var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","你同意了"+var1.getCallerId()+"的呼叫",true));

                    }
                });
            }

            @Override
            public void onRemoteInvitationRefused(final RemoteInvitation var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","你拒绝了"+var1.getCallerId()+"的呼叫",true));

                    }
                });
            }

            @Override
            public void onRemoteInvitationCanceled(final RemoteInvitation var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialogCall!=null&&dialogCall.isShowing()){
                            dialogCall.dismiss();
                        }
                        Toast.makeText(CallManageActivity.this,var1.getCallerId()+"已取消呼叫",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onRemoteInvitationFailure(RemoteInvitation var1, int var2) {

            }
        });
    }

    public void cancle(View view) {
        rtmCallManager.cancelLocalInvitation(localInvitation, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void var1) {

            }

            @Override
            public void onFailure(ErrorInfo var1) {

            }
        });
    }

    public void send(View view) {

         localInvitation=rtmCallManager.createLocalInvitation(et_uesr.getText().toString());
         rtmCallManager.sendLocalInvitation(localInvitation, new ResultCallback<Void>() {
             @Override
             public void onSuccess(Void var1) {

             }

             @Override
             public void onFailure(ErrorInfo var1) {

             }
         });


    }

    public void addData(MessageBean bean){
        messageApdater.addData(bean);
        linearLayoutManager.scrollToPositionWithOffset(messageApdater.getItemCount() - 1, Integer.MIN_VALUE);
    }

    public void showCallDialog(final RemoteInvitation remoteInvitation){
        final AlertDialog.Builder builder = new AlertDialog.Builder(CallManageActivity.this);
        builder.setTitle("提示");
        builder.setMessage("收到"+remoteInvitation.getCallerId()+"的呼叫");
        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rtmCallManager.acceptRemoteInvitation(remoteInvitation,null);
                messageApdater.addData(new MessageBean("","你同意了"+remoteInvitation.getCallerId()+"的呼叫",true));
                dialog.dismiss();

            }
        });
        builder.setNeutralButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rtmCallManager.refuseRemoteInvitation(remoteInvitation,null);
                messageApdater.addData(new MessageBean("","你拒绝了"+remoteInvitation.getCallerId()+"的呼叫",true));
                dialog.dismiss();
            }
        });
        dialogCall = builder.show();
    }
}