package com.jain.mylibexample;

import android.content.pm.PackageManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
  public static final int RequestPermissionCode = 1;


  private boolean checkPermission() {
    int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
        RECORD_AUDIO);
    return  result1 == PackageManager.PERMISSION_GRANTED;
  }

  private void requestPermission() {
    ActivityCompat.requestPermissions(MainActivity.this, new
        String[]{ RECORD_AUDIO}, RequestPermissionCode);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
      String permissions[], int[] grantResults) {
    switch (requestCode) {
      case RequestPermissionCode:
        if (grantResults.length> 0) {
          boolean RecordPermission = grantResults[0] ==
              PackageManager.PERMISSION_GRANTED;

          if ( RecordPermission) {
            Toast.makeText(MainActivity.this, "Permission Granted",
                Toast.LENGTH_LONG).show();
          } else {
            Toast.makeText(MainActivity.this,"Permission Denied",
                Toast.LENGTH_LONG).show();
          }
        }
        break;
    }
  }

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      if(checkPermission()) {
      //RECORD_AUDIO Permission granted,you can proceed further
      }else {
        requestPermission();
      }

        TextView tvMsg=findViewById(R.id.tv_msg);
        tvMsg.setText("Launcher Activity");

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);

                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}