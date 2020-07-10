package org.ar.rtm;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserAttActivity extends AppCompatActivity {

    RtmClient rtmClient;
    RecyclerView rvList;
    private MessageApdater messageApdater;
    private LinearLayoutManager linearLayoutManager;
    private String userId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("属性相关设置");
        setContentView(R.layout.activity_user_att);
        rtmClient = ARApplication.the().getChatManager().getRtmClient();
        rvList =findViewById(R.id.rv_list);
        messageApdater = new MessageApdater();
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.scrollToPositionWithOffset(messageApdater.getItemCount() - 1, Integer.MIN_VALUE);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setAdapter(messageApdater);
        if (!TextUtils.isEmpty(SharePrefUtil.getString("user"))){
            userId=SharePrefUtil.getString("user");
        }else {
            Toast.makeText(this,"请重新登陆",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void setAttr(View view) {
        List<RtmAttribute> list = new ArrayList<>();
        list.add(new RtmAttribute("name","liuxiaozhong"));
        list.add(new RtmAttribute("sex","男"));
        rtmClient.setLocalUserAttributes(list, new ResultCallback<Void>() {
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
        List<RtmAttribute> list = new ArrayList<>();
        list.add(new RtmAttribute("name","刘亦菲"));
        list.add(new RtmAttribute("sex","女"));
        rtmClient.addOrUpdateLocalUserAttributes(list, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void var1) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       addData(new MessageBean("","更新成功 属性\nname=刘亦菲\nsex=女",true));
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
        List<String> list = new ArrayList<>();
        list.add("name");
        rtmClient.deleteLocalUserAttributesByKeys(list, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","删除属性 name 成功",true));
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

    public void cleanLocal(View view) {
        rtmClient.clearLocalUserAttributes(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","属性全部清除完毕",true));
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

    public void getuserattr(View view) {
        rtmClient.getUserAttributes(userId, new ResultCallback<List<RtmAttribute>>() {
            @Override
            public void onSuccess(final List<RtmAttribute> var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","获取所有属性成功\n",true));
                        for (int i = 0 ;i<var1.size();i++){
                            addData(new MessageBean("","key="+var1.get(i).getKey()+"\nvalue="+var1.get(i).getValue(),true));
                        }

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

    public void getuservalue(View view) {
        List<String> list = new ArrayList<>();
        list.add("name");
        rtmClient.getUserAttributesByKeys(userId, list, new ResultCallback<List<RtmAttribute>>() {
            @Override
            public void onSuccess(final List<RtmAttribute> var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","获取name属性成功\n",true));
                        for (int i = 0 ;i<var1.size();i++){
                            addData(new MessageBean("","key="+var1.get(i).getKey()+"\nvalue="+var1.get(i).getValue(),true));
                        }

                    }
                });

            }

            @Override
            public void onFailure(ErrorInfo var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addData(new MessageBean("","获取失败",false));
                    }
                });
            }
        });
    }

    public void addData(MessageBean bean){
        messageApdater.addData(bean);
        linearLayoutManager.scrollToPositionWithOffset(messageApdater.getItemCount() - 1, Integer.MIN_VALUE);
    }
}