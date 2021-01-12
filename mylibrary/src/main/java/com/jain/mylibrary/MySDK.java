package com.jain.mylibrary;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.jain.mylibrary.Constants.RIDE_AUTH_SDK_API_KEY;

public class MySDK {
    public static final String TAG = "MyLibrary";
    public static final String FCM_PROJECT_NUMBER = "900290742842";
    public static final int ACCOUNT_EXIST = 0;
    public static final int ACCOUNT_NOT_ADDED_YET = 1;
    public static final int ACCOUNT_ADDED_SUCCESSFULLY = 2;
    public static final int ACTIVATION_CODE_INVALID = 3;
    public static final int NETWORK_ERROR = 4;
    public static final int RIDE_NOT_EXIST = 5;
    public static final int SUCCESS = 6;
    public static final int USER_VERIFIED = 7;
    public static final int USER_NOT_VERIFIED = 8;
    public static final int RIDE_DENIED = 9;
    public static final int ERROR = 10;
    public static final int RIDE_VERIFIED = 11;
    public static final int RIDE_WAITING = 12;
    public static final int NOT_INITIALIZED = 13;
    public static final int ACCOUNT_TYPE_USER = 14;
    public static final int ACCOUNT_TYPE_DRIVER = 15;
    private static final String FCM_PROJECT_ID = "mylib-5e1f8";
    private static final String FCM_ADD_ID = "1:900290742842:android:bbd88ecf326f9764ebc303";
    private static final String FCM_API_KEY = "AIzaSyDe9IlsQtcNnTSGWM5zbIUbG6JdCypkeI4";
    private static final String FCM_TOKEN = "fcm_token";
    private static final int debugLevel = MySDK.LogLevel.INFO.intValue();
    private static final String LIB_PACKAGE = "com.jain.mylibrary";
    private static volatile MySDK instance;
    private final ExecutorService es;
    private final Context context;
    //    private Persistence persistence;
//    private Gson gson;
    private final boolean isInitialized = false;
    private long EXECUTOR_THREAD_ID = 0;
    private String fcmToken = "";
    private boolean isAccountValid=false;


    private MySDK(Context context) {
        this.context = context;
        this.es = Executors.newFixedThreadPool(1);
    }

    public static MySDK getInstance(Context context) {
        MySDK result = instance;
        if (result != null) {
            return result;
        }
        synchronized (MySDK.class) {
            if (instance == null) {
                instance = new MySDK(context);
            }
            return instance;
        }
    }

    /**
     * Returns the log level set for MySDK
     *
     * @return The {@link MySDK.LogLevel} int value
     */
    @SuppressWarnings("WeakerAccess")
    public static int getDebugLevel() {
        return debugLevel;
    }

    // other static handlers
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public static void handleNotificationClicked(Context context, Bundle notification) {
//        if (notification == null) {
//            return;
//        }
//
//        String _accountId = null;
//        try {
//            _accountId = notification.getString(Constants.WZRK_ACCT_ID_KEY);
//        } catch (Throwable t) {
//            // no-op
//        }
//
//        if (instance == null) {
//            MySDK instance = createInstanceIfAvailable(context, _accountId);
//            if (instance != null) {
//                instance.pushNotificationClickedEvent(notification);
//            }
//            return;
//        }
//
//        for (String accountId : instance.keySet()) {
//            MySDK instance = MySDK.instance.get(accountId);
//            boolean shouldProcess = false;
//            if (instance != null) {
//                shouldProcess = (_accountId == null && instance.config.isDefaultInstance()) || instance.getAccountId()
//                        .equals(_accountId);
//            }
//            if (shouldProcess) {
//                instance.pushNotificationClickedEvent(notification);
//                break;
//            }
//        }
    }

    @SuppressWarnings("SameParameterValue")
    private static boolean isServiceAvailable(Context context, Class clazz) {
        if (clazz == null) {
            return false;
        }

        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();

        PackageInfo packageInfo;
        try {
            packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SERVICES);
            ServiceInfo[] services = packageInfo.services;
            for (ServiceInfo serviceInfo : services) {
                if (serviceInfo.name.equals(clazz.getName())) {
                    Logger.v("Service " + serviceInfo.name + " found");
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.d("Intent Service name not found exception - " + e.getLocalizedMessage());
        }
        return false;
    }

    public void init() {

        try {
            ApplicationInfo ai = null;
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            if (bundle.containsKey(RIDE_AUTH_SDK_API_KEY)) {
                String myApiKey = bundle.getString(RIDE_AUTH_SDK_API_KEY);
                if(Utils.isValidString(myApiKey)){
                   isAccountValid=true;
                }
                if (BuildConfig.DEBUG)
                    Toast.makeText(context, "myApiKey: " + myApiKey, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Dear developer. " +
                        "Don't forget to configure <meta-data android:name=\"api_key\" android:value=\"your_api_key\"/> in your AndroidManifest.xml file.", Toast.LENGTH_SHORT).show();
                return ;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Dear developer. " +
                    "Don't forget to configure <meta-data android:name=\"api_key\" android:value=\"your_api_key\"/> in your AndroidManifest.xml file.", Toast.LENGTH_SHORT).show();
            return ;
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId(FCM_PROJECT_ID)
                .setApplicationId(FCM_ADD_ID)
                .setApiKey(FCM_API_KEY)
                .build();

        boolean hasBeenInitialized = false;
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps(context);
        for (FirebaseApp app : firebaseApps) {
            if (app.getName().equals("secondary")) {
                hasBeenInitialized = true;
            }
        }

        if (!hasBeenInitialized) {
            // Initialize with secondary app
            FirebaseApp.initializeApp(context, options, "secondary");
        }
        // Retrieve secondary FirebaseApp
        FirebaseApp secondary = FirebaseApp.getInstance("secondary");
        FirebaseInstanceId.getInstance(secondary).getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        fcmToken = task.getResult().getToken();
                        Log.d(TAG, "sender token: " + fcmToken);

                        if (Utils.isValidString(fcmToken)) {
                            sendRegistrationToServer(context, fcmToken);
                            //getDefaultSharedPreferences(context).edit().
                            // putString(FCM_TOKEN, fcmToken).apply();
                        }
                    }
                });
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    public void sendRegistrationToServer(Context context, String token) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationToServer token: " + token);
        if (Utils.isValidString(token)) {
            if (Utils.isValidString(PreferencesUtils.getString(context, FCM_TOKEN))) {
                if (token.equals(PreferencesUtils.getString(context, FCM_TOKEN))) {
                    //Do nothing as token is same as saved token in preferences
                } else {
                    //save in preferences and send token to SDK server.

                    //save token in local preferences
                    PreferencesUtils.putString(context, FCM_TOKEN, fcmToken);

                    //make API call to send it server

                }
            }
        }

    }

    /**
     * Launches an asynchronous task to download the notification icon from MySDK,
     * and create the Android notification.
     * <p/>
     * Use this method when implementing your own FCM handling mechanism. Refer to the
     * SDK documentation for usage scenarios and examples.
     *
     * @param context A reference to an Android context
     * @param extras  The {@link Bundle} object received by the broadcast receiver
     */
    @SuppressWarnings({"WeakerAccess"})
    public void createNotification(final Context context, final Bundle extras) {
        if(isAccountValid){
            _createNotification(context, extras, Constants.EMPTY_NOTIFICATION_ID);
        }else{
            Toast.makeText(context,"Set Valid API KEY in manifest file",Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param context
     * @param extras
     * @param messageBody FCM message body received.
     */
    private void sendNotification(Context context, Bundle extras, String notifTitle, String messageBody, int id) {

        //Set Target Activity/Service from Lib
        String targetClass = extras.getString("target_activity");
        Intent intent = null;
        if (Utils.isValidString(targetClass)) {
            try {
                Class clazz =Class.forName(LIB_PACKAGE+"."+targetClass);
                intent = new Intent(context, clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        //If no Target Activity set, open Default Launcher Activity
        if (intent == null) {
            PackageManager pm = context.getPackageManager();
            intent = pm.getLaunchIntentForPackage(context.getPackageName());
        }

//        Intent intent = new Intent(context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = this.context.getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        int smallIcon = 0;
        try {
            String x = ManifestInfo.getInstance(this.context).getNotificationIcon();
            if (x == null) {
                throw new IllegalArgumentException();
            }
            smallIcon = this.context.getResources().getIdentifier(x, "drawable", this.context.getPackageName());
            if (smallIcon == 0) {
                smallIcon = R.mipmap.ic_launcher;
//                throw new IllegalArgumentException();
            }
        } catch (Throwable t) {
//            smallIcon = DeviceInfo.getAppIconAsIntId(context);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this.context, channelId)
                        .setSmallIcon(smallIcon)
                        .setContentTitle(notifTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "MySDK Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }

    /**
     * Use this to safely post a runnable to the async handler.
     * It adds try/catch blocks around the runnable and the handler itself.
     */
    @SuppressWarnings("UnusedParameters")
    private @Nullable
    Future<?> postAsyncSafely(final String name, final Runnable runnable) {
        Future<?> future = null;
        try {
            final boolean executeSync = Thread.currentThread().getId() == EXECUTOR_THREAD_ID;

            if (executeSync) {
                runnable.run();
            } else {
                future = es.submit(new Runnable() {
                    @Override
                    public void run() {
                        EXECUTOR_THREAD_ID = Thread.currentThread().getId();
                        try {
                            runnable.run();
                        } catch (Throwable t) {
//                            getConfigLogger().verbose(getAccountId(),
//                                    "Executor service: Failed to complete the scheduled task", t);
                        }
                    }
                });
            }
        } catch (Throwable t) {
//            getConfigLogger().verbose(getAccountId(), "Failed to submit task to the executor service", t);
        }

        return future;
    }


    private void _createNotification(final Context context, final Bundle extras, final int notificationId) {
        if (extras == null) {
            return;
        }

        try {
            postAsyncSafely("MySDK#_createNotification", new Runnable() {
                @Override
                public void run() {
                    try {
                        String notifMessage = extras.getString(Constants.NOTIF_MSG);
                        notifMessage = (notifMessage != null) ? notifMessage : "";
                        if (notifMessage.isEmpty()) {
                        }
                        String notifTitle = extras.getString(Constants.NOTIF_TITLE, "");
                        notifTitle = notifTitle.isEmpty() ? context.getApplicationInfo().name : notifTitle;
                        int tempId = 0;
                        if (notificationId == Constants.EMPTY_NOTIFICATION_ID) {
                            tempId = (int) (Math.random() * 100);
                        }

                        sendNotification(context, extras, notifTitle, notifMessage, tempId);
                    } catch (Throwable t) {

                        Log.d(TAG, "run: " + t);
                    }
                }
            });
        } catch (Throwable t) {
            Log.d(TAG, "run: " + t);
        }
    }

    public boolean isAccountValid() {
        return isAccountValid;
    }

    public void setAccountValid(boolean accountValid) {
        isAccountValid = accountValid;
    }


    @SuppressWarnings({"unused"})
    public enum LogLevel {
        OFF(-1),
        INFO(0),
        DEBUG(2);

        private final int value;

        LogLevel(final int newValue) {
            value = newValue;
        }

        public int intValue() {
            return value;
        }
    }
}
