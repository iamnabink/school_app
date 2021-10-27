package com.swipecrafts.school.utils;

/**
 * Created by Madhusudan Sapkota on 2/18/2018.
 */

public class Constants {

    public static final String APP_DATABASE_NAME = "SchoolAppDatabase.db";
    public static final String APP_PREF_NAME = "SchoolAppPref";

    public final static String BASE_URL = "http://app.thebrilliantonline.com/public/";

    public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // Notification Topic
    public static final String EVENT_NOTIFICATION = "event";
    public static final String GENERAL_NOTIFICATION = "_notification";
    public static final String TEACHER_NOTIFICATION = "teacher_notification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final int UNKNOWN_ERROR_CODE = 5000;
    public static final String UNKNOWN_ERROR_MESSAGE = "sorry an unexpected error occurred.";
    public static final String NO_INTERNET_MESSAGE = "no internet connection.";

    public static final String ENGLISH_DAYS_REMAINING = " days left";

    public static final String ENGLISH_DAYS_AGO = " days ago";
    public static final String NEPALI_DAYS_REMAINING = " दिन बाँकी";
    public static final String NEPALI_DAYS_AGO = " दिन पहिले";
    public static final String ENGLISH_TODAY = " today";
    public static final String NEPALI_TODAY = " आज";
}
