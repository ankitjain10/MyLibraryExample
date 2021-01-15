# MyLibraryExample
To Test this project please download and load this project in AS.
Sample Library

**Set API Key to initialize library in manifest file**
```
<meta-data android:name="RIDE_AUTH_SDK_API_KEY" android:value="<your_api_key>"/>
```
**Set notification icon for FCM notification**
```
<meta-data android:name="RIDE_AUTH_SDK_NOTIFICATION_ICON" android:value="<your_launcher_icon_name>"/>
```
 **Create and initialize a MySDK instance.**
 ```
 MySDK mySDK = MySDK.getInstance(context);
 
 mySDK.init();
 ```      
 
 **For FCM please add below code in manifest File**
 ```
<service
        android:name="com.jain.mylibrary.pushnotifications.SDKFirebaseMessagingService"
        android:exported="false">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
</service>
 ```
 or if your app is already using Firebase messaging:
 
 ```
<service
        android:name=".AppFirebaseMessagingService"
        android:exported="false">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
</service>
 ```
 Also make changes in AppFirebaseMessagingService, need to extend SDKFirebaseMessagingService rather than FirebaseMessagingService
 ```
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
            // process message
            ...
            }
    }
    ...
    }
 ```
 
**For Launching Library Activity in app add below code in manifest file**
```
<activity android:name="com.jain.mylibrary.SDKActivity">
</activity>
```
Send activity name in data payload of FCM Message to launch Library Activity, by default it will open Launching Activity of App. 
```
{
    ...
    "data": {
             "nm": "SDK Body",
             "nt": "SDK Title",
             "wzrk_cid": "sdk_channel",
             "target_activity": "SDKActivity"
             }
     ...
}
```

For Testing please import API collection from below link
https://www.getpostman.com/collections/568abfac5dd4590c2529
