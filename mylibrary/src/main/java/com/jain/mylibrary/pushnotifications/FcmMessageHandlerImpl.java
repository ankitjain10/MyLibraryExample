package com.jain.mylibrary.pushnotifications;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.jain.mylibrary.MySDK;
import com.jain.mylibrary.pushnotifications.IFcmMessageHandler;

import java.util.Map;

import static com.jain.mylibrary.Constants.RIDE_AUTH_SDK_LOG_TAG;


/**
 * implementation of {@link IFcmMessageHandler}
 */
public class FcmMessageHandlerImpl implements IFcmMessageHandler {


    @Override
    public boolean onMessageReceived(final Context context, final RemoteMessage message) {
        Log.d(RIDE_AUTH_SDK_LOG_TAG, "onMessageReceived: "+message);
        boolean isSuccess = false;
        try {
            if (message.getData().size() > 0) {
                Bundle extras = new Bundle();
                for (Map.Entry<String, String> entry : message.getData().entrySet()) {
                    extras.putString(entry.getKey(), entry.getValue());
                }
                MySDK.getInstance(context).createNotification(context,extras);
            }
        } catch (Throwable t) {
        }
        return isSuccess;
    }

    @Override
    public boolean onNewToken(final Context applicationContext, final String token) {
        MySDK.getInstance(applicationContext).sendRegistrationToServer(applicationContext,token);

        return true;
    }

}