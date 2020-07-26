package org.ar.ar_rtm_tutorial_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SelectActivity extends AppCompatActivity {

    private EditText etId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        etId = findViewById(R.id.et_channle);
    }

    public void groupChat(View view) {
        if (etId.getText().toString().isEmpty()){
            Toast.makeText(this,"请输入频道Id",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(this,ChatActivity.class);
        i.putExtra("type",2);
        i.putExtra("id",etId.getText().toString());
        startActivity(i);
    }

    public void singleChat(View view) {
        if (etId.getText().toString().isEmpty()){
            Toast.makeText(this,"请输入对方Id",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(this,ChatActivity.class);
        i.putExtra("type",1);
        i.putExtra("id",etId.getText().toString());
        startActivity(i);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            RTMApplication.getInstance().getRtmManager().getRtmClient().logout(null);
            RTMApplication.getInstance().ReleaseRtmManager();
            RTMApplication.getInstance().setUserId("");
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}