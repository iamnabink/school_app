package com.swipecrafts.library.calendar.core;

import com.swipecrafts.school.utils.Utils;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Madhusudan Sapkota on 3/8/2018.
 */

public class ADCalendar extends BaseCalendar {

    private static final String[] days = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private static final String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static final String[] months_short = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static final int[] daysInMonth = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final String[] days_of_weeks = new String[]{"sunday", "monday", "tuesday", "thursday", "wednesday", "friday", "saturday"};
    private static final String[] days_of_weeks_short = new String[]{"sun", "mon", "tue", "thu", "wed", "fri", "sat"};
    private static TimeZone tz = TimeZone.getTimeZone("Asia/Katmandu");

    public ADCalendar() {
        super(2002, 4, 14, 0, 0, 0);
    }

    public ADCalendar(int year, int month, int day, int hour, int minute, int second) {
        super(year, month, day, hour, minute, second);
    }

    public ADCalendar(int year, int month, int day) {
        super(year, month, day);
    }

    public ADCalendar(Date date) {
        super(from(date, AD));
    }

    public static synchronized ADCalendar today() {
        ADCalendar convert;
        synchronized (BSCalendar.class) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar(tz);
            convert =  new ADCalendar();
            convert.setTime(gregorianCalendar.getTime());
        }
        return convert;
    }

    @Override
    public String[] days() {
        return days;
    }

    @Override
    public String[] daysOfWeeks() {
        return days_of_weeks;
    }

    @Override
    public String[] daysOfWeeksShort() {
        return days_of_weeks_short;
    }

    @Override
    protected String[] months() {
        return months;
    }

    @Override
    protected String[] monthsShort() {
        return months_short;
    }

    @Override
    public BaseCalendar getReferenceDate() {
        return new ADCalendar();
    }

    private boolean leapYear(int year){
        Object obj = ((year % 4 != 0 || year % 100 == 0) && year % 400 != 0) ? null : 1;
        return obj == null;
    }

    @Override
    public int getMaximumDaysInMonth() {
        int i = daysInMonth[this.month - 1];
        return (leapYear(this.year) || this.month != 2) ? i : 29;
    }

    @Override
    public int getMaximumDaysInMonth(int year, int month) {
        int i = daysInMonth[month - 1];
        return (leapYear(year) || month != 2) ? i : 29;
    }

    @Override
    public int getMaximumDaysInYear() {
        return Utils.addAll(daysInMonth) + (leapYear(this.year) ? 0 : 1);
    }

    @Override
    public int getMaximumDaysInYear(int year) {
        return Utils.addAll(daysInMonth) + (leapYear(year) ? 0 : 1);
    }

    @Override
    public int getFirstDayOfWeek() {
        return 0;
    }

    @Override
    public boolean isThisMonth() {
        ADCalendar today = ADCalendar.today();
        return month == today.getMonth();
    }

    @Override
    public int getType() {
        return AD;
    }
}
