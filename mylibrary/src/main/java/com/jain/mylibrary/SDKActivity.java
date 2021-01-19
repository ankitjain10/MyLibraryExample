package com.jain.mylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.jain.mylibrary.pushnotifications.MyService;

public class SDKActivity extends AppCompatActivity {

    private static final String TAG = SDKActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdk);

        TextView tvMsg=findViewById(R.id.tv_msg);
        tvMsg.setText("Second Activity");

        Button stopServiceButton = findViewById(R.id.btn_stop);
        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SDKActivity.this, MyService.class);
                intent.setAction(MyService.ACTION_STOP_FOREGROUND_SERVICE);
                startService(intent);
            }
        });
    }
}