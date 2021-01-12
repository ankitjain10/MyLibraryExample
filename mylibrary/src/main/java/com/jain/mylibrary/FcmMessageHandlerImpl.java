package com.jain.mylibrary;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static com.jain.mylibrary.ToasterMessage.TAG;


/**
 * implementation of {@link IFcmMessageHandler}
 */
public class FcmMessageHandlerImpl implements IFcmMessageHandler {


    @Override
    public boolean onMessageReceived(final Context context, final RemoteMessage message) {
        Log.d(TAG, "onMessageReceived: "+message);
        boolean isSuccess = false;
        try {
            if (message.getData().size() > 0) {
                Bundle extras = new Bundle();
                for (Map.Entry<String, String> entry : message.getData().entrySet()) {
                    extras.putString(entry.getKey(), entry.getValue());
                }
//                CleverTapAPI cleverTapAPI = CleverTapAPI
//                        .getGlobalInstance(context, getAccountIdFromNotificationBundle(extras));
//                NotificationInfo info = CleverTapAPI.getNotificationInfo(extras);
//
//                if (info.fromCleverTap) {
//                    if (cleverTapAPI != null) {
//                        cleverTapAPI.config().log(LOG_TAG,
//                                FCM_LOG_TAG + "received notification from CleverTap: " + extras.toString());
//                    } else {
//                        Logger.d(LOG_TAG, FCM_LOG_TAG + "received notification from CleverTap: " + extras.toString());
//                    }
//                    CleverTapAPI.createNotification(context, extras);
//                    isSuccess = true;
//                }
            }
        } catch (Throwable t) {
//            Logger.d(LOG_TAG, FCM_LOG_TAG + "Error parsing FCM message", t);
        }
        return isSuccess;
    }

    @Override
    public boolean onNewToken(final Context applicationContext, final String token) {
        boolean isSuccess = false;
        sendRegistrationToServer(token);

//        try {
//            CleverTapAPI.tokenRefresh(applicationContext, token, PushConstants.PushType.FCM);
//            Logger.d(LOG_TAG, FCM_LOG_TAG + "New token received from FCM - " + token);
//            isSuccess = true;
//        } catch (Throwable t) {
//            // do nothing
//            Logger.d(LOG_TAG, FCM_LOG_TAG + "Error onNewToken", t);
//        }
        return isSuccess;
    }

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "sendRegistrationToServer: "+token);
    }
}