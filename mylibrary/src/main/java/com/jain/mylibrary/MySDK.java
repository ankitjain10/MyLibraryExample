package com.jain.mylibrary;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.jain.mylibrary.pushnotifications.CTNotificationIntentService;
import com.jain.mylibrary.pushnotifications.CTPushNotificationReceiver;
import com.jain.mylibrary.pushnotifications.MyService;
import com.jain.mylibrary.pushnotifications.amp.CTBackgroundIntentService;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.jain.mylibrary.Constants.INTENT_SERVICE_CLASS;
import static com.jain.mylibrary.Constants.RIDE_AUTH_SDK_API_KEY;

public class MySDK {
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
  public static final String TAG = "MyLibrary";
  public static final String FCM_PROJECT_NUMBER = "900290742842";
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
  private boolean isAccountValid = false;

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
        if (Utils.isValidString(myApiKey)) {
          isAccountValid = true;
        }
        if (BuildConfig.DEBUG) {
          Toast.makeText(context, "myApiKey: " + myApiKey, Toast.LENGTH_SHORT).show();
        }
      } else {
        Toast.makeText(context, "Dear developer. " +
                "Don't forget to configure <meta-data android:name=\"api_key\" android:value=\"your_api_key\"/> in your AndroidManifest.xml file.",
            Toast.LENGTH_SHORT).show();
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
      Toast.makeText(context, "Dear developer. " +
              "Don't forget to configure <meta-data android:name=\"api_key\" android:value=\"your_api_key\"/> in your AndroidManifest.xml file.",
          Toast.LENGTH_SHORT).show();
      return;
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
    try {
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
                sendRegistrationToServer(fcmToken);
                //getDefaultSharedPreferences(context).edit().
                // putString(FCM_TOKEN, fcmToken).apply();
              }
            }
          });
    } catch (Exception e) {
      e.printStackTrace();
      Log.d(TAG, "Exception: " + e);
    }
  }

  /**
   * Persist token to third-party servers.
   * <p>
   * Modify this method to associate the user's FCM InstanceID token with any server-side account
   * maintained by your application.
   *
   * @param token The new token.
   */
  public void sendRegistrationToServer(String token) {
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
   * @param extras The {@link Bundle} object received by the broadcast receiver
   */
  @SuppressWarnings({ "WeakerAccess" })
  public void createNotification(final Context context, final Bundle extras) {
    if (isAccountValid) {
      if(extras.containsKey(INTENT_SERVICE_CLASS)){
        Intent intent = new Intent(context, MyService.class);
        intent.setAction(MyService.ACTION_START_FOREGROUND_SERVICE);
        //                startService(intent);
        ContextCompat.startForegroundService(context, intent);
      }else{
        _createNotification(context, extras, Constants.EMPTY_NOTIFICATION_ID);
      }
    } else {
      Toast.makeText(context, "Set Valid API KEY in manifest file", Toast.LENGTH_LONG).show();
    }
  }

  private void triggerNotification(Context context, Bundle extras, String notifMessage,
      String notifTitle,int notificationId) {
    NotificationManager notificationManager =
        (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

    if (notificationManager == null) {
      String notificationManagerError =
          "Unable to render notification, Notification Manager is null.";
      //getConfigLogger().debug(getAccountId(), notificationManagerError);
      return;
    }

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
        ValidationResult channelIdError = ValidationResultFactory.create(512, messageCode, value);
        //getConfigLogger().debug(getAccountId(), channelIdError.getErrorDesc());
        //validationResultStack.pushValidationResult(channelIdError);
        //return;
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
        //getConfigLogger()
        //    .verbose(getAccountId(), "Falling back to big text notification, couldn't fetch big picture",
        //        t);
      }
    } else {
      style = new NotificationCompat.BigTextStyle()
          .bigText(notifMessage);
    }

    int smallIcon;
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
      smallIcon = DeviceInfo.getAppIconAsIntId(context);
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
              //getConfigLogger().debug(getAccountId(),
              //    "Converting collapse_key: " + collapse_key + " to notificationId int: "
              //        + notificationId);
            } catch (NumberFormatException e) {
              notificationId = (collapse_key.toString().hashCode());
              //getConfigLogger().debug(getAccountId(),
              //    "Converting collapse_key: " + collapse_key + " to notificationId int: "
              //        + notificationId);
            }
          }
        }
      } catch (NumberFormatException e) {
        // no-op
      }
    } else {
      //getConfigLogger().debug(getAccountId(), "Have user provided notificationId: " + notificationId
      //    + " won't use collapse_key (if any) as basis for notificationId");
    }

    // if after trying collapse_key notification is still empty set to random int
    if (notificationId == Constants.EMPTY_NOTIFICATION_ID) {
      notificationId = (int) (Math.random() * 100);
      //getConfigLogger().debug(getAccountId(), "Setting random notificationId: " + notificationId);
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
        .setSmallIcon(smallIcon);

    //nb.setLargeIcon(Utils.getNotificationBitmap(icoPath, true, context));

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
      //getConfigLogger().debug(getAccountId(), "Could not process sound parameter", t);
    }

    // add actions if any
    JSONArray actions = null;
    String actionsString = extras.getString(Constants.WZRK_ACTIONS);
    if (actionsString != null) {
      try {
        actions = new JSONArray(actionsString);
      } catch (Throwable t) {
        //getConfigLogger()
        //    .debug(getAccountId(), "error parsing notification actions: " + t.getLocalizedMessage());
      }
    }
    String intentServiceName =null;
    if(extras.containsKey(INTENT_SERVICE_CLASS)){
       intentServiceName =extras.getString(INTENT_SERVICE_CLASS);
    }
    if(Utils.isNotValidString(intentServiceName)){
      intentServiceName = ManifestInfo.getInstance(context).getIntentServiceName();
    }
    Class clazz = null;
    if (intentServiceName != null) {
      try {
        clazz = Class.forName(intentServiceName);
      } catch (ClassNotFoundException e) {
        try {
          clazz = Class.forName(
              "com.jain.mylibrary.pushnotifications.CTNotificationIntentService");
        } catch (ClassNotFoundException ex) {
          Logger.d("No Intent Service found");
        }
      }
    } else {
      try {
        clazz =
            Class.forName("com.jain.mylibrary.pushnotifications.CTNotificationIntentService");
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
            //getConfigLogger().debug(getAccountId(),
            //    "not adding push notification action: action label or id missing");
            continue;
          }
          int icon = 0;
          if (!ico.isEmpty()) {
            try {
              icon =
                  context.getResources().getIdentifier(ico, "drawable", context.getPackageName());
            } catch (Throwable t) {
              //getConfigLogger().debug(getAccountId(),
              //    "unable to add notification action icon: " + t.getLocalizedMessage());
            }
          }

          boolean sendToCTIntentService = (autoCancel && isCTIntentServiceAvailable);

          Intent actionLaunchIntent;
          if (sendToCTIntentService) {
            actionLaunchIntent = new Intent(CTNotificationIntentService.MAIN_ACTION);
            actionLaunchIntent.setPackage(context.getPackageName());
            actionLaunchIntent.putExtra("ct_type", CTNotificationIntentService.TYPE_BUTTON_CLICK);
            if (!dl.isEmpty()) {
              actionLaunchIntent.putExtra("dl", dl);
            }
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

            actionLaunchIntent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
          //getConfigLogger()
          //    .debug(getAccountId(), "error adding notification action : " + t.getLocalizedMessage());
        }
      }
    }

    Notification n = nb.build();
    notificationManager.notify(notificationId, n);
    //getConfigLogger().debug(getAccountId(), "Rendered notification: " + n.toString());

    //String ttl = extras.getString(Constants.WZRK_TIME_TO_LIVE,
    //    (System.currentTimeMillis() + Constants.DEFAULT_PUSH_TTL) / 1000 + "");
    //long wzrk_ttl = Long.parseLong(ttl);
    //String wzrk_pid = extras.getString(Constants.WZRK_PUSH_ID);
    //DBAdapter dbAdapter = loadDBAdapter(context);
    //getConfigLogger().verbose("Storing Push Notification..." + wzrk_pid + " - with ttl - " + ttl);
    //dbAdapter.storePushNotificationId(wzrk_pid, wzrk_ttl);
    //
    //boolean notificationViewedEnabled = "true".equals(extras.getString(Constants.WZRK_RNV, ""));
    //if (!notificationViewedEnabled) {
    //  ValidationResult notificationViewedError = ValidationResultFactory
    //      .create(512, Constants.NOTIFICATION_VIEWED_DISABLED, extras.toString());
    //  getConfigLogger().debug(notificationViewedError.getErrorDesc());
    //  validationResultStack.pushValidationResult(notificationViewedError);
    //  return;
    //}
    //pushNotificationViewedEvent(extras);
  }

  /**
   * Create and show a simple notification containing the received FCM message.
   *
   * @param messageBody FCM message body received.
   */
  private void sendNotification(Context context, Bundle extras, String notifTitle,
      String messageBody, int id) {

    //Set Target Activity/Service from Lib
    String targetClass = extras.getString("target_activity");
    Intent intent = null;
    if (Utils.isValidString(targetClass)) {
      try {
        Class clazz = Class.forName(LIB_PACKAGE + "." + targetClass);
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
    PendingIntent pendingIntent =
        PendingIntent.getActivity(this.context, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT);

    String channelId = this.context.getString(R.string.default_notification_channel_id);
    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    int smallIcon = 0;
    try {
      String x = ManifestInfo.getInstance(this.context).getNotificationIcon();
      if (x == null) {
        throw new IllegalArgumentException();
      }
      smallIcon =
          this.context.getResources().getIdentifier(x, "drawable", this.context.getPackageName());
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

  private void _createNotification(final Context context, final Bundle extras,
      final int notificationId) {
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

            //sendNotification(context, extras, notifTitle, notifMessage, tempId);
            triggerNotification(context, extras, notifTitle, notifMessage, tempId);
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

  @RestrictTo(RestrictTo.Scope.LIBRARY)
  public static void runJobWork(Context context, JobParameters parameters) {
    getInstance(context).runInstanceJobWork(context,parameters);
    //if (instances == null) {
    //  CleverTapAPI instance = CleverTapAPI.getDefaultInstance(context);
    //  if (instance != null) {
    //    if (instance.getConfig().isBackgroundSync()) {
    //      instance.runInstanceJobWork(context, parameters);
    //    } else {
    //      Logger.d("Instance doesn't allow Background sync, not running the Job");
    //    }
    //  }
    //  return;
    //}
    //for (String accountId : CleverTapAPI.instances.keySet()) {
    //  CleverTapAPI instance = CleverTapAPI.instances.get(accountId);
    //  if (instance != null && instance.getConfig().isAnalyticsOnly()) {
    //    Logger.d(accountId, "Instance is Analytics Only not running the Job");
    //    continue;
    //  }
    //  if (!(instance != null && instance.getConfig().isBackgroundSync())) {
    //    Logger.d(accountId, "Instance doesn't allow Background sync, not running the Job");
    //    continue;
    //  }
    //  instance.runInstanceJobWork(context, parameters);
    //}
  }

  private void runInstanceJobWork(final Context context, final JobParameters parameters) {
    postAsyncSafely("runningJobService", new Runnable() {
      @Override
      public void run() {
        //if (pushProviders.isNotificationSupported()) {
        //  Logger.v(getAccountId(), "Token is not present, not running the Job");
        //  return;
        //}

        Calendar now = Calendar.getInstance();

        int hour = now.get(Calendar.HOUR_OF_DAY); // Get hour in 24 hour format
        int minute = now.get(Calendar.MINUTE);

        Date currentTime = parseTimeToDate(hour + ":" + minute);
        Date startTime = parseTimeToDate(Constants.DND_START);
        Date endTime = parseTimeToDate(Constants.DND_STOP);

        //if (isTimeBetweenDNDTime(startTime, endTime, currentTime)) {
        //  Logger.v(getAccountId(), "Job Service won't run in default DND hours");
        //  return;
        //}

        long lastTS = /*loadDBAdapter(context).getLastUninstallTimestamp()*/0;

        if (lastTS == 0 || lastTS > System.currentTimeMillis() - 24 * 60 * 60 * 1000) {
          try {
            JSONObject eventObject = new JSONObject();
            eventObject.put("bk", 1);
            //queueEvent(context, eventObject, Constants.PING_EVENT);

            if (parameters == null) {
              int pingFrequency = 240/*getPingFrequency(context)*/;
              AlarmManager alarmManager = (AlarmManager) context
                  .getSystemService(Context.ALARM_SERVICE);
              Intent cancelIntent = new Intent(CTBackgroundIntentService.MAIN_ACTION);
              cancelIntent.setPackage(context.getPackageName());
              PendingIntent alarmPendingIntent = PendingIntent
                  .getService(context,1 /*getAccountId().hashCode()*/, cancelIntent,
                      PendingIntent.FLAG_UPDATE_CURRENT);
              if (alarmManager != null) {
                alarmManager.cancel(alarmPendingIntent);
              }
              Intent alarmIntent = new Intent(CTBackgroundIntentService.MAIN_ACTION);
              alarmIntent.setPackage(context.getPackageName());
              PendingIntent alarmServicePendingIntent = PendingIntent
                  .getService(context, 1/*getAccountId().hashCode()*/, alarmIntent,
                      PendingIntent.FLAG_UPDATE_CURRENT);
              if (alarmManager != null) {
                if (pingFrequency != -1) {
                  alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                      SystemClock.elapsedRealtime() + (pingFrequency
                          * Constants.ONE_MIN_IN_MILLIS),
                      Constants.ONE_MIN_IN_MILLIS * pingFrequency, alarmServicePendingIntent);
                }
              }
            }
          } catch (JSONException e) {
            Logger.v("Unable to raise background Ping event");
          }

        }
      }
    });
  }

  private Date parseTimeToDate(String time) {

    final String inputFormat = "HH:mm";
    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
    try {
      return inputParser.parse(time);
    } catch (java.text.ParseException e) {
      return new Date(0);
    }
  }

  @RestrictTo(RestrictTo.Scope.LIBRARY)
  public static void runBackgroundIntentService(Context context) {
    MySDK.getInstance(context).runInstanceJobWork(context,null);
    //if (instances == null) {
    //  CleverTapAPI instance = CleverTapAPI.getDefaultInstance(context);
    //  if (instance != null) {
    //    if (instance.getConfig().isBackgroundSync()) {
    //      instance.runInstanceJobWork(context, null);
    //    } else {
    //      Logger.d("Instance doesn't allow Background sync, not running the Job");
    //    }
    //  }
    //  return;
    //}
    //for (String accountId : CleverTapAPI.instances.keySet()) {
    //  CleverTapAPI instance = CleverTapAPI.instances.get(accountId);
    //  if (instance == null) {
    //    continue;
    //  }
    //  if (instance.getConfig().isAnalyticsOnly()) {
    //    Logger.d(accountId, "Instance is Analytics Only not processing device token");
    //    continue;
    //  }
    //  if (!instance.getConfig().isBackgroundSync()) {
    //    Logger.d(accountId, "Instance doesn't allow Background sync, not running the Job");
    //    continue;
    //  }
    //  instance.runInstanceJobWork(context, null);
    //}
  }



  @SuppressWarnings({ "unused" })
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
