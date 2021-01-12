package com.jain.mylibrary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MySDK {
    private static volatile MySDK instance;
    public static final String TAG = "MyLibrary";

    public static final String FCM_PROJECT_NUMBER = "900290742842";
    private static final String FCM_PROJECT_ID = "mylib-5e1f8";
    private static final String FCM_ADD_ID = "1:900290742842:android:bbd88ecf326f9764ebc303";
    private static final String FCM_API_KEY = "AIzaSyDe9IlsQtcNnTSGWM5zbIUbG6JdCypkeI4";
    private static final String FCM_TOKEN = "fcm_token";

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
    private long EXECUTOR_THREAD_ID = 0;
    private final ExecutorService es;

    private Context context;
//    private Persistence persistence;
//    private Gson gson;
    private boolean isInitialized = false;
    private String fcmToken = "";

    private MySDK(Context context) {
        this.context = context;
        this.es = Executors.newFixedThreadPool(1);

//        persistence = new Persistence(context);
    }




    public static MySDK getInstance(Context context) {
        MySDK result = instance;
        if (result != null) {
            return result;
        }
        synchronized(MySDK.class) {
            if (instance == null) {
                instance = new MySDK(context);
            }
            return instance;
        }
    }

    public void init() {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId(FCM_PROJECT_ID)
                .setApplicationId(FCM_ADD_ID)
                .setApiKey(FCM_API_KEY)
                .build();

        boolean hasBeenInitialized=false;
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps(context);
        for(FirebaseApp app : firebaseApps){
            if(app.getName().equals("secondary")){
                hasBeenInitialized=true;
            }
        }

        if(!hasBeenInitialized) {
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
                        Log.d(TAG, "sender token: "+fcmToken);

                        if(Utils.isValidString(fcmToken)){
                            sendRegistrationToServer(context,fcmToken);
                            PreferencesUtils.putString(context,FCM_TOKEN, fcmToken);
                            //getDefaultSharedPreferences(context).edit().
                            // putString(FCM_TOKEN, fcmToken).apply();
                        }
                    }
                });
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    public static void sendRegistrationToServer(Context context,String token) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationToServer token: "+token);
    }


    private static int debugLevel = MySDK.LogLevel.INFO.intValue();

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
    public  void createNotification(final Context context, final Bundle extras) {
        createNotification(context, extras, Constants.EMPTY_NOTIFICATION_ID);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param context
     * @param messageBody FCM message body received.
     */
    private void sendNotification(Context context, String notifTitle, String messageBody, int id) {

        PackageManager pm = context.getPackageManager();
        Intent intent=pm.getLaunchIntentForPackage(context.getPackageName());

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
//                smallIcon=context.getDrawable(R.mipmap.ic_launcher);
//                throw new IllegalArgumentException();
            }
        } catch (Throwable t) {
//            smallIcon = DeviceInfo.getAppIconAsIntId(context);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this.context, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notifTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) this.context. getSystemService(Context.NOTIFICATION_SERVICE);

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




    /**
     * Launches an asynchronous task to download the notification icon from MySDK,
     * and create the Android notification.
     * <p>
     * If your app is using MySDK SDK's built in FCM message handling,
     * this method does not need to be called explicitly.
     * <p/>
     * Use this method when implementing your own FCM handling mechanism. Refer to the
     * SDK documentation for usage scenarios and examples.
     *
     * @param context        A reference to an Android context
     * @param extras         The {@link Bundle} object received by the broadcast receiver
     * @param notificationId A custom id to build a notification
     */
    @SuppressWarnings({"WeakerAccess"})
    public  void createNotification(final Context context, final Bundle extras, final int notificationId) {
        _createNotification(context, extras, notificationId);
    }

    private void _createNotification(final Context context, final Bundle extras, final int notificationId) {
        if (extras == null /*|| extras.get(Constants.NOTIFICATION_TAG) == null*/) {
            return;
        }

//        if (config.isAnalyticsOnly()) {
//            getConfigLogger().debug(getAccountId(), "Instance is set for Analytics only, cannot create notification");
//            return;
//        }

        try {
            postAsyncSafely("MySDK#_createNotification", new Runnable() {
                @Override
                public void run() {
                    try {
//                        getConfigLogger().debug(getAccountId(), "Handling notification: " + extras.toString());
//                        dbAdapter = loadDBAdapter(context);
//                        if (extras.getString(Constants.WZRK_PUSH_ID) != null) {
//                            if (dbAdapter.doesPushNotificationIdExist(extras.getString(Constants.WZRK_PUSH_ID))) {
//                                getConfigLogger().debug(getAccountId(),
//                                        "Push Notification already rendered, not showing again");
//                                return;
//                            }
//                        }
                        String notifMessage = extras.getString(Constants.NOTIF_MSG);
                        notifMessage = (notifMessage != null) ? notifMessage : "";
                        if (notifMessage.isEmpty()) {
                            //silent notification
//                            getConfigLogger()
//                                    .verbose(getAccountId(), "Push notification message is empty, not rendering");
//                            loadDBAdapter(context).storeUninstallTimestamp();
//                            String pingFreq = extras.getString("pf", "");
//                            if (!TextUtils.isEmpty(pingFreq)) {
//                                updatePingFrequencyIfNeeded(context, Integer.parseInt(pingFreq));
//                            }
//                            return;
                        }
                        String notifTitle = extras.getString(Constants.NOTIF_TITLE, "");
                        notifTitle = notifTitle.isEmpty() ? context.getApplicationInfo().name : notifTitle;
int tempId=0;
                        if (notificationId == Constants.EMPTY_NOTIFICATION_ID) {
                            tempId = (int) (Math.random() * 100);
                        }

                        sendNotification(context,notifTitle,notifMessage,tempId);
//                        triggerNotification(context, extras, notifMessage, notifTitle, notificationId);
                    } catch (Throwable t) {
                        // Occurs if the notification image was null
                        // Let's return, as we couldn't get a handle on the app's icon
                        // Some devices throw a PackageManager* exception too
//                        getConfigLogger().debug(getAccountId(), "Couldn't render notification: ", t);
                    }
                }
            });
        } catch (Throwable t) {
//            getConfigLogger().debug(getAccountId(), "Failed to process push notification", t);
        }
    }



    public void triggerNotification(Context context, Bundle extras, String notifMessage, String notifTitle,
                                     int notificationId) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

//        if (notificationManager == null) {
//            String notificationManagerError = "Unable to render notification, Notification Manager is null.";
//            getConfigLogger().debug(getAccountId(), notificationManagerError);
//            return;
//        }

        String channelId = extras.getString(Constants.WZRK_CHANNEL_ID, "");
        boolean requiresChannelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int messageCode = -1;
            String value = "";

            if (channelId.isEmpty()) {
                messageCode = Constants.CHANNEL_ID_MISSING_IN_PAYLOAD;
                value = extras.toString();
            } else if (notificationManager.getNotificationChannel(channelId) == null) {
                messageCode = Constants.CHANNEL_ID_NOT_REGISTERED;
                value = channelId;
            }
            if (messageCode != -1) {
//                ValidationResult channelIdError = ValidationResultFactory.create(512, messageCode, value);
//                getConfigLogger().debug(getAccountId(), channelIdError.getErrorDesc());
//                validationResultStack.pushValidationResult(channelIdError);
//                return;
            }
        }

        String icoPath = extras.getString(Constants.NOTIF_ICON);
        Intent launchIntent = new Intent(context, CTPushNotificationReceiver.class);

        PendingIntent pIntent;

        // Take all the properties from the notif and add it to the intent
        launchIntent.putExtras(extras);
        launchIntent.removeExtra(Constants.WZRK_ACTIONS);
        pIntent = PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(),
                launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Style style;
        String bigPictureUrl = extras.getString(Constants.WZRK_BIG_PICTURE);
        if (bigPictureUrl != null && bigPictureUrl.startsWith("http")) {
            try {
                Bitmap bpMap = Utils.getNotificationBitmap(bigPictureUrl, false, context);

                if (bpMap == null) {
                    throw new Exception("Failed to fetch big picture!");
                }

                if (extras.containsKey(Constants.WZRK_MSG_SUMMARY)) {
                    String summaryText = extras.getString(Constants.WZRK_MSG_SUMMARY);
                    style = new NotificationCompat.BigPictureStyle()
                            .setSummaryText(summaryText)
                            .bigPicture(bpMap);
                } else {
                    style = new NotificationCompat.BigPictureStyle()
                            .setSummaryText(notifMessage)
                            .bigPicture(bpMap);
                }
            } catch (Throwable t) {
                style = new NotificationCompat.BigTextStyle()
                        .bigText(notifMessage);
//                getConfigLogger()
//                        .verbose(getAccountId(), "Falling back to big text notification, couldn't fetch big picture",
//                                t);
            }
        } else {
            style = new NotificationCompat.BigTextStyle()
                    .bigText(notifMessage);
        }

        int smallIcon = 0;
        try {
            String x = ManifestInfo.getInstance(context).getNotificationIcon();
            if (x == null) {
                throw new IllegalArgumentException();
            }
            smallIcon = context.getResources().getIdentifier(x, "drawable", context.getPackageName());
            if (smallIcon == 0) {
                throw new IllegalArgumentException();
            }
        } catch (Throwable t) {
//            smallIcon = DeviceInfo.getAppIconAsIntId(context);
        }

        int priorityInt = NotificationCompat.PRIORITY_DEFAULT;
        String priority = extras.getString(Constants.NOTIF_PRIORITY);
        if (priority != null) {
            if (priority.equals(Constants.PRIORITY_HIGH)) {
                priorityInt = NotificationCompat.PRIORITY_HIGH;
            }
            if (priority.equals(Constants.PRIORITY_MAX)) {
                priorityInt = NotificationCompat.PRIORITY_MAX;
            }
        }

        // if we have no user set notificationID then try collapse key
        if (notificationId == Constants.EMPTY_NOTIFICATION_ID) {
            try {
                Object collapse_key = extras.get(Constants.WZRK_COLLAPSE);
                if (collapse_key != null) {
                    if (collapse_key instanceof Number) {
                        notificationId = ((Number) collapse_key).intValue();
                    } else if (collapse_key instanceof String) {
                        try {
                            notificationId = Integer.parseInt(collapse_key.toString());
//                            getConfigLogger().debug(getAccountId(),
//                                    "Converting collapse_key: " + collapse_key + " to notificationId int: "
//                                            + notificationId);
                        } catch (NumberFormatException e) {
                            notificationId = (collapse_key.toString().hashCode());
//                            getConfigLogger().debug(getAccountId(),
//                                    "Converting collapse_key: " + collapse_key + " to notificationId int: "
//                                            + notificationId);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                // no-op
            }
        } else {
//            getConfigLogger().debug(getAccountId(), "Have user provided notificationId: " + notificationId
//                    + " won't use collapse_key (if any) as basis for notificationId");
        }

        // if after trying collapse_key notification is still empty set to random int
        if (notificationId == Constants.EMPTY_NOTIFICATION_ID) {
            notificationId = (int) (Math.random() * 100);
//            getConfigLogger().debug(getAccountId(), "Setting random notificationId: " + notificationId);
        }

        NotificationCompat.Builder nb;
        if (requiresChannelId) {
            nb = new NotificationCompat.Builder(context, channelId);

            // choices here are Notification.BADGE_ICON_NONE = 0, Notification.BADGE_ICON_SMALL = 1, Notification.BADGE_ICON_LARGE = 2.  Default is  Notification.BADGE_ICON_LARGE
            String badgeIconParam = extras.getString(Constants.WZRK_BADGE_ICON, null);
            if (badgeIconParam != null) {
                try {
                    int badgeIconType = Integer.parseInt(badgeIconParam);
                    if (badgeIconType >= 0) {
                        nb.setBadgeIconType(badgeIconType);
                    }
                } catch (Throwable t) {
                    // no-op
                }
            }

            String badgeCountParam = extras.getString(Constants.WZRK_BADGE_COUNT, null);
            if (badgeCountParam != null) {
                try {
                    int badgeCount = Integer.parseInt(badgeCountParam);
                    if (badgeCount >= 0) {
                        nb.setNumber(badgeCount);
                    }
                } catch (Throwable t) {
                    // no-op
                }
            }
            if (extras.containsKey(Constants.WZRK_SUBTITLE)) {
                nb.setSubText(extras.getString(Constants.WZRK_SUBTITLE));
            }
        } else {
            // noinspection all
            nb = new NotificationCompat.Builder(context);
        }

        if (extras.containsKey(Constants.WZRK_COLOR)) {
            int color = Color.parseColor(extras.getString(Constants.WZRK_COLOR));
            nb.setColor(color);
            nb.setColorized(true);
        }

        nb.setContentTitle(notifTitle)
                .setContentText(notifMessage)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setStyle(style)
                .setPriority(priorityInt)
                .setSmallIcon(R.mipmap.ic_launcher);

        nb.setLargeIcon(Utils.getNotificationBitmap(icoPath, true, context));

        try {
            if (extras.containsKey(Constants.WZRK_SOUND)) {
                Uri soundUri = null;

                Object o = extras.get(Constants.WZRK_SOUND);

                if ((o instanceof Boolean && (Boolean) o)) {
                    soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                } else if (o instanceof String) {
                    String s = (String) o;
                    if (s.equals("true")) {
                        soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    } else if (!s.isEmpty()) {
                        if (s.contains(".mp3") || s.contains(".ogg") || s.contains(".wav")) {
                            s = s.substring(0, (s.length() - 4));
                        }
                        soundUri = Uri
                                .parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName()
                                        + "/raw/" + s);

                    }
                }

                if (soundUri != null) {
                    nb.setSound(soundUri);
                }
            }
        } catch (Throwable t) {
//            getConfigLogger().debug(getAccountId(), "Could not process sound parameter", t);
        }

        // add actions if any
        JSONArray actions = null;
        String actionsString = extras.getString(Constants.WZRK_ACTIONS);
        if (actionsString != null) {
            try {
                actions = new JSONArray(actionsString);
            } catch (Throwable t) {
//                getConfigLogger()
//                        .debug(getAccountId(), "error parsing notification actions: " + t.getLocalizedMessage());
            }
        }

        String intentServiceName = ManifestInfo.getInstance(context).getIntentServiceName();
        Class clazz = null;
        if (intentServiceName != null) {
            try {
                clazz = Class.forName(intentServiceName);
            } catch (ClassNotFoundException e) {
                try {
                    clazz = Class.forName("com.clevertap.android.sdk.pushnotification.CTNotificationIntentService");
                } catch (ClassNotFoundException ex) {
                    Logger.d("No Intent Service found");
                }
            }
        } else {
            try {
                clazz = Class.forName("com.clevertap.android.sdk.pushnotification.CTNotificationIntentService");
            } catch (ClassNotFoundException ex) {
                Logger.d("No Intent Service found");
            }
        }

        boolean isCTIntentServiceAvailable = isServiceAvailable(context, clazz);

        if (actions != null && actions.length() > 0) {
            for (int i = 0; i < actions.length(); i++) {
                try {
                    JSONObject action = actions.getJSONObject(i);
                    String label = action.optString("l");
                    String dl = action.optString("dl");
                    String ico = action.optString(Constants.NOTIF_ICON);
                    String id = action.optString("id");
                    boolean autoCancel = action.optBoolean("ac", true);
                    if (label.isEmpty() || id.isEmpty()) {
//                        getConfigLogger().debug(getAccountId(),
//                                "not adding push notification action: action label or id missing");
                        continue;
                    }
                    int icon = 0;
                    if (!ico.isEmpty()) {
                        try {
                            icon = context.getResources().getIdentifier(ico, "drawable", context.getPackageName());
                        } catch (Throwable t) {
//                            getConfigLogger().debug(getAccountId(),
//                                    "unable to add notification action icon: " + t.getLocalizedMessage());
                        }
                    }

                    boolean sendToCTIntentService = (autoCancel && isCTIntentServiceAvailable);

                    Intent actionLaunchIntent;
                    if (false/*sendToCTIntentService*/) {
//                        actionLaunchIntent = new Intent(CTNotificationIntentService.MAIN_ACTION);
//                        actionLaunchIntent.setPackage(context.getPackageName());
//                        actionLaunchIntent.putExtra("ct_type", CTNotificationIntentService.TYPE_BUTTON_CLICK);
//                        if (!dl.isEmpty()) {
//                            actionLaunchIntent.putExtra("dl", dl);
//                        }
                    } else {
                        if (!dl.isEmpty()) {
                            actionLaunchIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dl));
                        } else {
                            actionLaunchIntent = context.getPackageManager()
                                    .getLaunchIntentForPackage(context.getPackageName());
                        }
                    }

                    if (actionLaunchIntent != null) {
                        actionLaunchIntent.putExtras(extras);
                        actionLaunchIntent.removeExtra(Constants.WZRK_ACTIONS);
                        actionLaunchIntent.putExtra("actionId", id);
                        actionLaunchIntent.putExtra("autoCancel", autoCancel);
                        actionLaunchIntent.putExtra("wzrk_c2a", id);
                        actionLaunchIntent.putExtra("notificationId", notificationId);

                        actionLaunchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    }

                    PendingIntent actionIntent;
                    int requestCode = ((int) System.currentTimeMillis()) + i;
                    if (sendToCTIntentService) {
                        actionIntent = PendingIntent.getService(context, requestCode,
                                actionLaunchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    } else {
                        actionIntent = PendingIntent.getActivity(context, requestCode,
                                actionLaunchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    }
                    nb.addAction(icon, label, actionIntent);

                } catch (Throwable t) {
//                    getConfigLogger()
//                            .debug(getAccountId(), "error adding notification action : " + t.getLocalizedMessage());
                }
            }
        }

        Notification n = nb.build();
        notificationManager.notify(notificationId, n);
//        getConfigLogger().debug(getAccountId(), "Rendered notification: " + n.toString());

//        String ttl = extras.getString(Constants.WZRK_TIME_TO_LIVE,
//                (System.currentTimeMillis() + Constants.DEFAULT_PUSH_TTL) / 1000 + "");
//        long wzrk_ttl = Long.parseLong(ttl);
//        String wzrk_pid = extras.getString(Constants.WZRK_PUSH_ID);
//        DBAdapter dbAdapter = loadDBAdapter(context);
//        getConfigLogger().verbose("Storing Push Notification..." + wzrk_pid + " - with ttl - " + ttl);
//        dbAdapter.storePushNotificationId(wzrk_pid, wzrk_ttl);
//
//        boolean notificationViewedEnabled = "true".equals(extras.getString(Constants.WZRK_RNV, ""));
//        if (!notificationViewedEnabled) {
//            ValidationResult notificationViewedError = ValidationResultFactory
//                    .create(512, Constants.NOTIFICATION_VIEWED_DISABLED, extras.toString());
//            getConfigLogger().debug(notificationViewedError.getErrorDesc());
//            validationResultStack.pushValidationResult(notificationViewedError);
//            return;
//        }
//        pushNotificationViewedEvent(extras);
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
