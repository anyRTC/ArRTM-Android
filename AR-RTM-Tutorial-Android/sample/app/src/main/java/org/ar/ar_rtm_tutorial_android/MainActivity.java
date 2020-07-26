package org.ar.ar_rtm_tutorial_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.ar.rtm.ErrorInfo;
import org.ar.rtm.ResultCallback;

public class MainActivity extends AppCompatActivity {

    private EditText etUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUser = findViewById(R.id.et_user);
    }

    public void Login(View view) {
        if (TextUtils.isEmpty(getString(R.string.rtm_app_id))){
            Toast.makeText(this,"请先设置appId",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(etUser.getText().toString())){
            Toast.makeText(this,"请输入Id",Toast.LENGTH_SHORT).show();
            return;
        }

        RTMApplication.getInstance().setUserId(etUser.getText().toString());

        RTMApplication.getInstance().getRtmManager().getRtmClient().login("", etUser.getText().toString(), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void var1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this,SelectActivity.class));
                    }
                });
            }

            @Override
            public void onFailure(ErrorInfo var1) {
            }
        });


    }
}