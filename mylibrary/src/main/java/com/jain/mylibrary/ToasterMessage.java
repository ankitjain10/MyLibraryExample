package com.jain.mylibrary;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ToasterMessage {

    public static void s(Context c, String message){
        try {
            ApplicationInfo ai = null;
            ai = c.getPackageManager().getApplicationInfo(c.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String myApiKey = bundle.getString("my_test_metagadata");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("TAG", "Dear developer. " +
                    "Don't forget to configure <meta-data android:name=\"my_test_metagadata\" android:value=\"testValue\"/> in your AndroidManifest.xml file.");

        }
        Toast.makeText(c,message,Toast.LENGTH_SHORT).show();


    }
}