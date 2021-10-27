package com.swipecrafts.school.utils.fcm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swipecrafts.school.ui.MainActivity;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.LogUtils;
import com.swipecrafts.school.utils.NotificationUtil;

import java.lang.reflect.Type;
import java.util.Calendar;

/**
 * Created by Madhusudan Sapkota on 2/16/2018.
 */

public class FCMService extends FirebaseMessagingService {

    private static final String TAG = FCMService.class.getSimpleName();
    private NotificationUtil notificationUtil;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        String token = FirebaseInstanceId.getInstance().getToken();
        if (token == null || TextUtils.isEmpty(token)) return;
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            handleDataMessage(remoteMessage.getData().toString());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification());
        }
    }

    private void handleNotification(RemoteMessage.Notification message) {
        if (!NotificationUtil.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message.getBody());
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtil notificationUtil = new NotificationUtil(getApplicationContext());
            notificationUtil.playNotificationSound();

            Intent intent = new Intent(this, MainActivity.class);
            notificationUtil.showNotificationMessage(message.getTitle(), message.getBody(), String.valueOf(Calendar.getInstance().getTimeInMillis()), intent);
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(String notificationData) {
        Log.e(TAG, "push data: " + notificationData);
        NotificationRes notiRes;
        try {
            Type responseType = new TypeToken<NotificationRes>() {}.getType();
            notiRes = new Gson().fromJson(notificationData, responseType);
        }catch (Exception e){
            LogUtils.errorLog("FCMNotification", e.getMessage());
            return;
        }
        NotificationRes.NotificationData data = notiRes.getNotificationData();
        LogUtils.errorLog(TAG, "data "+data.getMessage()+" "+ data.getImageUrl());


        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        resultIntent.putExtra("message", data.getMessage());
        String time = data.getTimestamp();

        // check for image attachment
        if (TextUtils.isEmpty(data.getImageUrl())) {
            showNotificationMessage(getApplicationContext(), data.getTitle(), data.getMessage(), time, resultIntent);
        } else {
            // image is present, show notification with image
            showNotificationMessageWithBigImage(getApplicationContext(), data.getTitle(), data.getMessage(), time, resultIntent, data.getImageUrl());
        }

//        if (!NotificationUtil.isAppIsInBackground(getApplicationContext())) {
//            // app is in foreground, broadcast the push message
//            Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
//            pushNotification.putExtra("message", data.getMessage());
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//            // play notification sound
//            NotificationUtil notificationUtil = new NotificationUtil(getApplicationContext());
//            notificationUtil.playNotificationSound();
//        } else {
//            // app is in background, show the notification in notification tray
//            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
//            resultIntent.putExtra("message", data.getMessage());
//
//            String time = data.getTimestamp();
//
//            // check for image attachment
//            if (TextUtils.isEmpty(data.getImageUrl())) {
//                showNotificationMessage(getApplicationContext(), data.getTitle(), data.getMessage(), time, resultIntent);
//            } else {
//                // image is present, show notification with image
//                showNotificationMessageWithBigImage(getApplicationContext(), data.getTitle(), data.getMessage(), time, resultIntent, data.getImageUrl());
//            }
//        }
    }


    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        Log.e(TAG, "Showing notification with text only!!");
        NotificationUtil notificationUtil = new NotificationUtil(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtil.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        NotificationUtil notificationUtil = new NotificationUtil(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtil.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

}
