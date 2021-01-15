package com.jain.mylibrary.pushnotifications;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.jain.mylibrary.MySDK.FCM_PROJECT_NUMBER;

/**
 * Clevertap's Implementation for Firebase Message service
 */
public class SDKFirebaseMessagingService extends FirebaseMessagingService {

    private IFcmMessageHandler mHandler = new FcmMessageHandlerImpl();

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        //check If message is from SDK senderId only
        if(message.getFrom().equals(FCM_PROJECT_NUMBER))
        mHandler.onMessageReceived(getApplicationContext(), message);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        mHandler.onNewToken(getApplicationContext(), token);
    }
}
