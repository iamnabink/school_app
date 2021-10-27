package com.swipecrafts.school.utils;

import android.util.Log;

/**
 * Created by Madhusudan Sapkota on 4/8/2018.
 */
public class LogUtils {

    private static final boolean isLogVisible = true;

    private LogUtils() {
    }

    public static void errorLog(String TAG, String message) {
        if (isLogVisible) Log.e(TAG, message);
    }

    public static void errorLog(String TAG, String message, Throwable throwable) {
        if (isLogVisible) Log.e(TAG, message, throwable);
    }

    public static void debugLog(String TAG, String message) {
        if (isLogVisible) Log.d(TAG, message);
    }

    public static void debugLog(String TAG, String message, Throwable throwable) {
        if (isLogVisible) Log.d(TAG, message, throwable);
    }

    public static void infoLog(String TAG, String message) {
        if (isLogVisible) Log.i(TAG, message);
    }

    public static void infoLog(String TAG, String message, Throwable throwable) {
        if (isLogVisible) Log.i(TAG, message, throwable);
    }

    public static void verboseLog(String TAG, String message) {
        if (isLogVisible) Log.v(TAG, message);
    }

    public static void verboseLog(String TAG, String message, Throwable throwable) {
        if (isLogVisible) Log.v(TAG, message, throwable);
    }
}
