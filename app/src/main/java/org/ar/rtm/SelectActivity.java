package org.ar.rtm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SelectActivity extends AppCompatActivity {

    private EditText et_channle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        et_channle=findViewById(R.id.et_channle);

    }



    public void exit(View view) {
        ARApplication.the().getChatManager().getRtmClient().logout(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void var1) {

            }

            @Override
            public void onFailure(ErrorInfo var1) {

            }
        });
        finish();
    }

    public void oneTone(View view) {
        if (et_channle.getText().toString().isEmpty()){
            return;
        }
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("type",1);
        i.putExtra("id",et_channle.getText().toString());
        startActivity(i);
    }

    public void group(View view) {
        if (et_channle.getText().toString().isEmpty()){
            return;
        }
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("type",2);
        i.putExtra("id",et_channle.getText().toString());
        startActivity(i);
    }

    public void call(View view) {
        Intent i = new Intent(this,CallManageActivity.class);
        startActivity(i);
    }

    public void query(View view) {
        Intent i = new Intent(this,QueryActivity.class);
        startActivity(i);
    }

    public void user_attr(View view) {
        Intent i = new Intent(this,UserAttActivity.class);
        startActivity(i);
    }

    public void channel_attr(View view) {
        Intent i = new Intent(this, ChannelAttrActivity.class);
        startActivity(i);
    }
}