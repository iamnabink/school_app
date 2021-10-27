package com.swipecrafts.library.calendar.core;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Madhusudan Sapkota on 4/12/2018.
 */
public class CalendarUtils {

    private CalendarUtils(){}

    public static Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static void setToFirstDay(BaseCalendar calendar) {
        calendar.setTime(calendar.year, calendar.month, 1);
    }

    public static void setToLastDay(BaseCalendar calendar) {
        calendar.setTime(calendar.year, calendar.month, calendar.getMaximumDaysInMonth());
    }

    public static boolean isToday(Date date){
        Date today = new Date();
        return date.equals(today);
    }

    public static Date setToFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static Date setToLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static int getDayOfWeek(CalendarDay tempWorkingCalendar) {
        return tempWorkingCalendar.getActiveCalendar().getDayOfWeek();
    }
}
