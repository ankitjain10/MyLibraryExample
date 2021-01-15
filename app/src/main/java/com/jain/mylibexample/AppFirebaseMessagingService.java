package com.jain.mylibexample;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.RemoteMessage;

import com.jain.mylibrary.pushnotifications.SDKFirebaseMessagingService;
import java.util.Map;

import static com.jain.mylibrary.MySDK.FCM_PROJECT_NUMBER;

/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 *
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 *
 * <intent-filter>
 *   <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class AppFirebaseMessagingService extends SDKFirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /*remoteMessage will be handled by SDKFirebaseMessagingService in case remoteMessage.getFrom()
        matches with SDK Project Number/SenderId*/
        if (remoteMessage.getFrom().equals(FCM_PROJECT_NUMBER)) {
            super.onMessageReceived(remoteMessage);
        } else {
            // TODO(developer): Handle FCM messages here.
            // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                createAppNotification(remoteMessage.getData());
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }

            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
        }
    }

    private void createAppNotification(Map<String, String> data) {
        String title=data.get("title");
        String message=data.get("message");
        if(title!=null&&message!=null){

        int tempId = (int) (Math.random() * 100);

        //Set Target Activity/Service
        Intent intent = null;

        //If no Target Activity set, open Default Launcher Activity
        if (intent == null) {
            PackageManager pm = getPackageManager();
            intent = pm.getLaunchIntentForPackage(getPackageName());
        }

        //        Intent intent = new Intent(context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent
            pendingIntent = PendingIntent.getActivity(this,
            0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_channel_id);

        NotificationCompat.Builder notificationBuilder =
            new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
            (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                "App Channel",
                NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(tempId /* ID of notification */, notificationBuilder.build());}
    }



    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

}

