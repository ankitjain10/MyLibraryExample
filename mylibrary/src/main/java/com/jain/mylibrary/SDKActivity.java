package com.jain.mylibrary;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SDKActivity extends AppCompatActivity {

    private static final String TAG = SDKActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvMsg=findViewById(R.id.tv_msg);
        tvMsg.setText("Second Activity");
    }
}