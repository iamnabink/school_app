package com.swipecrafts.library.calendar.core;

import android.annotation.SuppressLint;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by Madhusudan Sapkota on 4/12/2018.
 */

@SuppressLint("UniqueConstants")
@Retention(RetentionPolicy.SOURCE)
@IntDef(flag = true, value = {BaseCalendar.AD, BaseCalendar.BS})
public @interface CalendarType {
}
