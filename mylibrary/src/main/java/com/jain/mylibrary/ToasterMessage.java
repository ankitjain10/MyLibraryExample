package com.jain.mylibrary;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

public class ToasterMessage {
    private static String API_KEY="api_key";
    public static final String TAG = "MyLibrary";

    public static void showToastMesssage(Context c, String message){
        try {
            ApplicationInfo ai = null;
            ai = c.getPackageManager().getApplicationInfo(c.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            if(bundle.containsKey(API_KEY)){
                String myApiKey = bundle.getString(API_KEY);
//                Toast.makeText(c,"myApiKey: "+myApiKey,Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(c,"Dear developer. " +
                        "Don't forget to configure <meta-data android:name=\"api_key\" android:value=\"your_api_key\"/> in your AndroidManifest.xml file.",Toast.LENGTH_SHORT).show();
            return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(c,"Dear developer. " +
                    "Don't forget to configure <meta-data android:name=\"api_key\" android:value=\"your_api_key\"/> in your AndroidManifest.xml file.",Toast.LENGTH_SHORT).show();
            return;
        }
//        Toast.makeText(c,message,Toast.LENGTH_SHORT).show();


    }
}