package com.swipecrafts.school.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.google.android.gms.location.LocationRequest;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Madhusudan Sapkota on 3/22/2018.
 */

public class Utils {

    @SuppressLint("DefaultLocale")
    public static String formatTime(float sec) {
        int minutes = (int) (sec / 60);
        int seconds = (int) (sec % 60);
        return String.format("%d:%02d", minutes, seconds);
    }

    public static boolean isOnline(Context context) {
        NetworkInfo networkInfo = getNetworkType(context);
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static NetworkInfo getNetworkType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connMgr.getActiveNetworkInfo();
    }

    public static int getLocationMode(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }


        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (TextUtils.isEmpty(locationProviders)) {
                locationMode = LocationRequest.PRIORITY_NO_POWER;
            }
            else if (locationProviders.contains(LocationManager.GPS_PROVIDER) && locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                locationMode = LocationRequest.PRIORITY_HIGH_ACCURACY;
            }
            else if (locationProviders.contains(LocationManager.GPS_PROVIDER)) {
                locationMode = LocationRequest.PRIORITY_LOW_POWER;
            }
            else if (locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                locationMode = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
            }

        }

        return locationMode;
    }

    public static int getDaysDifference(Date from, Date to) {
        long msDiff = from.getTime() - to.getTime();
        return (int) TimeUnit.MILLISECONDS.toDays(msDiff);
    }

    public static int addAll(int[] items) {
        int total = 0;
        for (Integer i : items) {
            total += i;
        }
        return total;
    }
}
