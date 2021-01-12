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
 
 MySDK mySDK = MySDK.getInstance(_instance);
 
 mySDK.init();
         
 
 **For FCM please add below code in manifest File**
 
         <service
            android:name="com.jain.mylibrary.pushnotifications.FcmMessageListenerService">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
        </service>


**For Launching Library Activity in app add below code in manifest file**

        <activity android:name="com.jain.mylibrary.SDKActivity">
        </activity>


For Testing please import API collection from below link
https://www.getpostman.com/collections/568abfac5dd4590c2529
