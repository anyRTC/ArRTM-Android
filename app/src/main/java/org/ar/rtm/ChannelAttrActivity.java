package org.ar.rtm;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelAttrActivity extends AppCompatActivity {

    private EditText etChannel;
    RtmClient rtmClient;
    RecyclerView rvList;
    private MessageApdater messageApdater;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_attr);
        setTitle("频道属性设置");
        etChannel = findViewById(R.id.et_channel);
        rvList=findViewById(R.id.rv_list);
        messageApdater = new MessageApdater();
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.scrollToPositionWithOffset(messageApdater.getItemCount() - 1, Integer.MIN_VALUE);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setAdapter(messageApdater);
        rtmClient = ARApplication.the().getChatManager().getRtmClient();
    }

    public void setAttr(View view) {
        if (etChannel.getText().toString().isEmpty()){
            return;
        }
        List<RtmChannelAttribute> list = new ArrayList<>();
        list.add(new RtmChannelAttribute("name","liuxiaozhong"));
        list.add(new RtmChannelAttribute("sex","男"));

        rtmClient.setChannelAttributes(etChannel.getText().toString(), list, new ChannelAttributeOptions(false), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","设置属性成功\nname=liuxiaozhong\nsex=男",true));
                    }
                });
            }

            @Override
            public void onFailure(ErrorInfo var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","设置失败",false));
                    }
                });
            }
        });
    }

    public void addOrUpdate(View view) {
        if (etChannel.getText().toString().isEmpty()){
            return;
        }
        List<RtmChannelAttribute> list = new ArrayList<>();
        list.add(new RtmChannelAttribute("name","刘亦菲"));
        list.add(new RtmChannelAttribute("sex","女"));

        rtmClient.addOrUpdateChannelAttributes(etChannel.getText().toString(), list, new ChannelAttributeOptions(true), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","设置属性成功\nname=刘亦菲\nsex=女",true));
                    }
                });
            }

            @Override
            public void onFailure(ErrorInfo var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","设置失败",false));
                    }
                });
            }
        });


    }

    public void deleteLocal(View view) {
        if (etChannel.getText().toString().isEmpty()){
            return;
        }
        List<String> list = new ArrayList<>();
        list.add("name");
        rtmClient.deleteChannelAttributesByKeys(etChannel.getText().toString(), list, new ChannelAttributeOptions(true), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","删除属性成功",true));
                    }
                });
            }

            @Override
            public void onFailure(ErrorInfo var1) {

            }
        });
    }

    public void cleanChannel(View view) {
        if (etChannel.getText().toString().isEmpty()){
            return;
        }
        rtmClient.clearChannelAttributes(etChannel.getText().toString(), new ChannelAttributeOptions(true), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","清除属性成功",true));
                    }
                });
            }

            @Override
            public void onFailure(ErrorInfo var1) {

            }
        });
    }

    public void getChannelattr(View view) {
        if (etChannel.getText().toString().isEmpty()){
            return;
        }
        rtmClient.getChannelAttributes(etChannel.getText().toString(), new ResultCallback<List<RtmChannelAttribute>>() {
            @Override
            public void onSuccess(final List<RtmChannelAttribute> var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (var1.size() > 0) {
                            for (int i = 0; i < var1.size(); i++) {
                                addData(new MessageBean("", "获取属性成功" + var1.get(i).getKey() + ":" + var1.get(i).getValue(), true));
                            }
                        }else {
                            addData(new MessageBean("", "获取属性成功====没有属性" , true));
                        }
                    }

                });
            }

            @Override
            public void onFailure(ErrorInfo var1) {

            }
        });
    }

    public void getChannelvalue(View view) {
        if (etChannel.getText().toString().isEmpty()){
            return;
        }
        List<String> list = new ArrayList<>();
        list.add("name");
        rtmClient.getChannelAttributesByKeys(etChannel.getText().toString(), list,new ResultCallback<List<RtmChannelAttribute>>() {
            @Override
            public void onSuccess(final List<RtmChannelAttribute> var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (var1.size() > 0) {
                            for (int i = 0; i < var1.size(); i++) {
                                addData(new MessageBean("", "获取属性成功" + var1.get(i).getKey() + ":" + var1.get(i).getValue(), true));
                            }
                        }else {
                            addData(new MessageBean("", "获取属性成功====没有属性" , true));
                        }
                    }

                });
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
}