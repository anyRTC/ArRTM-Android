package org.ar.rtm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUser = findViewById(R.id.et_user);
        if (!TextUtils.isEmpty(SharePrefUtil.getString("user"))){
            etUser.setText(SharePrefUtil.getString("user"));
            etUser.setSelection(SharePrefUtil.getString("user").length());
        }
    }

    public void Login(View view) {
        if (etUser.getText().toString().isEmpty()){
            Toast.makeText(this,"请输入ID",Toast.LENGTH_SHORT).show();
        }else {
            if (ARApplication.the().getChatManager().getRtmClient()==null){
                ARApplication.the().getChatManager().init();
            }
            ARApplication.the().getChatManager().getRtmClient().login("", etUser.getText().toString(), new ResultCallback<Void>() {
                @Override
                public void onSuccess(Void var1) {
                    SharePrefUtil.putString("user",etUser.getText().toString());
                    startActivity(new Intent(LoginActivity.this,SelectActivity.class));

                }

                @Override
                public void onFailure(ErrorInfo var1) {

                }
            });
        }
    }

    public void Release(View view) {
        if (ARApplication.the().getChatManager().getRtmClient()==null){
            ARApplication.the().getChatManager().getRtmClient().release();
        }
        finish();
        System.exit(0);
    }
}