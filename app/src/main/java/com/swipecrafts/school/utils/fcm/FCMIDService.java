package com.swipecrafts.school.utils.fcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.LogUtils;

/**
 * Created by Madhusudan Sapkota on 2/16/2018.
 */

public class
FCMIDService extends FirebaseInstanceIdService {
    private final String TAG = "FCMIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        LogUtils.infoLog(TAG, "FCM Refreshed token: " + refreshedToken);

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        storeRegIdInPref(refreshedToken);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.APP_PREF_NAME, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("FCMRegId", token);
        editor.apply();
    }
}
