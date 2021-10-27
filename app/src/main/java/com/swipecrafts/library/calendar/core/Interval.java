package com.swipecrafts.library.calendar.core;

import java.util.Calendar;
import java.util.Date;

import static com.swipecrafts.library.calendar.core.ADCalendar.daysInMonth;

/**
 * Created by Madhusudan Sapkota on 6/14/2018.
 */
public class Interval {
    private Interval() {
    }

    public static String getFormattedDateDifference(Calendar from, Calendar to) {
        Calendar fD;
        Calendar tD;
        String type;

        if (from.equals(to)) {
            return "now";
        } else if (from.before(to)) {
            fD = from;
            tD = to;
            type = "ago";
        } else {
            tD = from;
            fD = to;
            type = "left";
        }

        return getFormattedDateDifference(
                fD.get(Calendar.YEAR), fD.get(Calendar.MONTH), fD.get(Calendar.DATE), fD.get(Calendar.HOUR), fD.get(Calendar.MINUTE),
                tD.get(Calendar.YEAR), tD.get(Calendar.MONTH), tD.get(Calendar.DATE), tD.get(Calendar.HOUR), tD.get(Calendar.MINUTE)
        ) + " " + type;
    }

    public static String getFormattedDateDifference(Date from, Date to) {
        Calendar fromDate = Calendar.getInstance();
        fromDate.setTime(from);
        Calendar toDate = Calendar.getInstance();
        toDate.setTime(to);

        return getFormattedDateDifference(fromDate, toDate);
    }

    public static String getFormattedDateDifference(BaseCalendar from, BaseCalendar to) {
        BaseCalendar fD;
        BaseCalendar tD;
        String type;

        if (from.equals(to)) {
            return "now";
        } else if (from.before(to)) {
            fD = from;
            tD = to;
            type = "ago";
        } else {
            tD = from;
            fD = to;
            type = "left";
        }

        return getFormattedDateDifference(
                fD.getYear(), fD.getMonth(), fD.getDay(), fD.getHour(), fD.getMinute(),
                tD.getYear(), tD.getMonth(), tD.getDay(), tD.getHour(), tD.getMinute()
        ) + " " + type;
    }

    public static String getFormattedDateDifference(
            int fromY, int fromM, int fromD, int fromH, int fromMin,
            int toY, int toM, int toD, int toH, int toMin
    ) {
        String formattedValue;

        int year;
        int month;
        int day;
        int hour;
        int minute;

        hour = toH - fromH;
        minute = toMin - fromMin;

        int increment = 0;

        if (fromD > toD) {
            increment = daysInMonth[fromM];
        }

        if (increment != 0) {
            // checking Leap Year
            if (((fromY % 4 != 0 || fromY % 100 == 0) && fromY % 400 != 0) || (fromM != 1)) {
                increment = daysInMonth[fromM];
            } else {
                increment = 29;
            }
        }

        if (increment != 0) {
            day = (toD + increment) - fromD;
            increment = 1;
        } else {
            day = toD - fromD;
        }

        if ((fromM + increment) > toM) {
            month = (toM + 12) - (fromM + increment);
            increment = 1;
        } else {
            month = (toM) - (fromM + increment);
            increment = 0;
        }

        year = toY - (fromY + increment);


        if (year == 0 && month == 0 && day == 0 && hour == 0)
            formattedValue = minute + " minute(s) ";
        else if (year == 0 && month == 0 && day == 0) formattedValue = hour + " hour(s) ";
        else if (year == 0 && month == 0) formattedValue = day + " day(s)";
        else if (year == 0) formattedValue = month + " month(s), " + day + " day(s)";
        else formattedValue = year + " Year(s), " + month + " month(s), " + day + " day(s)";

        return formattedValue;
    }
}
