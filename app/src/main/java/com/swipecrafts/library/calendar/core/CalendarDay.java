package com.swipecrafts.library.calendar.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Madhusudan Sapkota on 3/8/2018.
 */

public class CalendarDay implements Parcelable {

    public static final Creator<CalendarDay> CREATOR = new Creator<CalendarDay>() {
        public CalendarDay createFromParcel(Parcel in) {
            return new CalendarDay(in);
        }

        public CalendarDay[] newArray(int size) {
            return new CalendarDay[size];
        }
    };

    private final ADCalendar ad;
    private final BSCalendar bs;

    @CalendarType
    private int type;

    private transient Date _date;
    private transient Calendar _calendar;

    public CalendarDay(BaseCalendar calendar) {

        if (calendar.getType() == BaseCalendar.AD) {
            this.ad = (ADCalendar) calendar.clone();
            bs = (BSCalendar) BaseCalendar.convert(calendar, BaseCalendar.BS);
            bs.setTime(ad.getTime());
        } else {
            this.bs = (BSCalendar) calendar.clone();
            this.ad = (ADCalendar) BaseCalendar.convert(bs, BaseCalendar.AD);
        }
        this.type = calendar.getType();
    }

    public CalendarDay(ADCalendar adCalendar, BSCalendar bsCalendar, int type) {
        this.ad = (ADCalendar) adCalendar.clone();
        this.bs = (BSCalendar) bsCalendar.clone();
        this.type = type;
    }

    public CalendarDay(Parcel in) {
        int y = in.readInt();
        int m = in.readInt();
        int d = in.readInt();
        int t = in.readInt();

        this.type = t;

        if (t == BaseCalendar.AD) {
            this.ad = new ADCalendar(y, m, d);
            this.bs = new BSCalendar();
            this.bs.setTime(ad.getTime());
        } else {
            this.bs = new BSCalendar(y, m, d);
            this.ad = (ADCalendar) BaseCalendar.convert(bs, BaseCalendar.AD);
        }
    }

    public static CalendarDay today() {
        CalendarDay day =  new CalendarDay(ADCalendar.today());
        day.setType(BaseCalendar.BS);
        return day;
    }

    public static CalendarDay from(@Nullable BaseCalendar calendar) {
        if (calendar == null) {
            return null;
        }
        return new CalendarDay(calendar);
    }

    public static CalendarDay from(@Nullable Calendar calendar, @CalendarType int type) {
        if (calendar == null) {
            return null;
        }
        return from(calendar.getTime(), type);
    }

    public static CalendarDay from(@Nullable Date date, @CalendarType int type) {
        if (date == null) {
            return null;
        }

        return new CalendarDay(type == BaseCalendar.AD ? new ADCalendar(date) : new BSCalendar(date));
    }

    public static CalendarDay from(@Nullable Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        return from(calendar.getTime());
    }

    public static CalendarDay from(@Nullable Date date) {
        if (date == null) {
            return null;
        }

        return new CalendarDay(new ADCalendar(date));
    }

    public static CalendarDay from(CalendarDay day) {
        return day == null ? null : new CalendarDay(day.getADCalendar(), day.getBSCalendar(), day.getType());
    }

    public static CalendarDay fromToMonth(@Nullable CalendarDay day) {
        if (day == null) return null;

        BSCalendar bs = (BSCalendar) day.getBSCalendar().clone();
        ADCalendar ad = (ADCalendar) day.getADCalendar().clone();

        int dayDiff = bs.day - 1;
        ad.subtractDay(dayDiff);
        bs.subtractDay(dayDiff);

        return new CalendarDay(ad, bs, day.getType());
    }

    public static void main(String[] args) {
        BSCalendar bs = BSCalendar.today();
        CalendarDay re = from(bs);
        System.out.println("First "+ re);
        System.out.println("Second "+fromToMonth(re));
    }

    public static CalendarDay fromToMaxMonth(CalendarDay day) {
        if (day == null) return null;

        BSCalendar bs = (BSCalendar) day.getBSCalendar().clone();
        ADCalendar ad = (ADCalendar) day.getADCalendar().clone();
        CalendarUtils.setToLastDay(bs);
        ad.setTime(bs.getTime());
        return new CalendarDay(ad, bs, day.getType());
    }

    public static CalendarDay from(int year, int month, int day, @CalendarType int type) {
        if (type == BaseCalendar.AD) {
            return from(new ADCalendar(year, month, day));
        } else {
            return from(new BSCalendar(year, month, day));
        }
    }

    public ADCalendar getADCalendar() {
        return ad;
    }

    public BSCalendar getBSCalendar() {
        return bs;
    }

    public int getType() {
        return type;
    }

    public void setType(@CalendarType int type) {
        this.type = type;
    }

    @NonNull
    public Date getDate() {
        if (_date == null) {
            _date = ad.getTime();
        }
        return _date;
    }

    @NonNull
    public Calendar getCalendar() {
        if (_calendar == null) {
            _calendar = Calendar.getInstance();
            _calendar.setTime(getDate());
        }
        return _calendar;
    }

    public void copyToMonthOnly(@NonNull Calendar calendar) {
        calendar.clear();
        calendar.set(ad.getYear(), ad.getMonth(), 1);
    }

    public void copyToMonthOnly(@NonNull ADCalendar calendar) {
        calendar.setTime(ad.getYear(), ad.getMonth(), 1);
    }

    public void copyToMonthOnly(@NonNull BSCalendar calendar) {
        calendar.setTime(bs.getYear(), bs.getMonth(), 1);
    }

    public void copyTo(@NonNull BaseCalendar baseCalendar) {
        if (BaseCalendar.AD == baseCalendar.getType())
            baseCalendar.setTime(ad.getYear(), ad.getMonth(), ad.getDay());
        else if (BaseCalendar.BS == baseCalendar.getType())
            baseCalendar.setTime(bs.getYear(), bs.getMonth(), bs.getDay());
    }

    public void copyTo(@NonNull CalendarDay calendarDay) {
        calendarDay.type = type;
        calendarDay.ad.setTime(ad.getTime());
        calendarDay.bs.setTime(bs.getYear(), bs.getMonth(), bs.getDay());
    }

    public boolean isInRange(@Nullable CalendarDay minDate, @Nullable CalendarDay maxDate) {
        return !(minDate != null && minDate.isAfter(this)) &&
                !(maxDate != null && maxDate.isBefore(this));
    }

    public boolean isBefore(@NonNull CalendarDay other) {
        if (other == null) {
            throw new IllegalArgumentException("other cannot be null");
        }

        if (getYear() == other.getYear()) {
            return ((getMonth() == other.getMonth()) ? (getDay() < other.getDay()) : (getMonth() < other.getMonth()));
        } else {
            return getYear() < other.getYear();
        }
    }

    public boolean isAfter(@NonNull CalendarDay other) {
        if (other == null) {
            throw new IllegalArgumentException("other cannot be null");
        }

        if (getYear() == other.getYear()) {
            return (getMonth() == other.getMonth()) ? (getDay() > other.getDay()) : (getMonth() > other.getMonth());
        } else {
            return getYear() > other.getYear();
        }
    }

    public boolean isToday() {
        CalendarDay today = CalendarDay.today();
        today.setType(getType());
        return this.equals(today);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CalendarDay that = (CalendarDay) o;

        return this.getActiveCalendar().equals(that.getActiveCalendar());
    }

    public BaseCalendar getActiveCalendar() {
        return type == BaseCalendar.AD ? ad : bs;
    }

    public BaseCalendar getInActiveCalendar() {
        return type == BaseCalendar.AD ? bs : ad;
    }

    public int getYear() {
        return getActiveCalendar().getYear();
    }

    public int getMonth() {
        return getActiveCalendar().getMonth();
    }

    public int getDay() {
        return getActiveCalendar().getDay();
    }

    public CharSequence getDisplayWeekName(int dayOfWeek, boolean isShort, boolean isActiveCalendar) {
        BaseCalendar active = getActiveCalendar();
        BaseCalendar inActive = getInActiveCalendar();
        return isShort ?
                isActiveCalendar ?
                        active.daysOfWeeksShort()[dayOfWeek] :
                        inActive.daysOfWeeksShort()[dayOfWeek] :
                !isActiveCalendar ?
                        active.daysOfWeeks()[dayOfWeek] :
                        inActive.daysOfWeeks()[dayOfWeek];
    }

    public String getDisplayDay(boolean isActiveCalendar) {
        return isActiveCalendar ?
                convertToString(getActiveCalendar(), getActiveCalendar().getDay()) :
                convertToString(getInActiveCalendar(), getInActiveCalendar().getDay());
    }

    public String getDisplayMonth(boolean isShort, boolean isActiveCalendar) {
        BaseCalendar active = getActiveCalendar();
        BaseCalendar inActive = getInActiveCalendar();

        return isShort ?
                isActiveCalendar ?
                        active.monthsShort()[active.getMonth() - 1] :
                        inActive.monthsShort()[inActive.getMonth() - 1] +"/"+ inActive.monthsShort()[inActive.getMonth() == 12 ? 0 : inActive.getMonth()] :
                isActiveCalendar ?
                        active.months()[active.getMonth() - 1] :
                        inActive.months()[inActive.getMonth() - 1];
    }

    public String getDisplayYear(boolean isActiveCalendar) {
        BaseCalendar active = getActiveCalendar();
        BaseCalendar inActive = getInActiveCalendar();

        return isActiveCalendar ?
                convertToString(active, active.getYear()) :
                convertToString(inActive, inActive.getYear());
    }

    @Override
    public int hashCode() {
        return type == BaseCalendar.AD ? ad.hashCode() : bs.hashCode();
    }

    @Override
    public String toString() {
        return "CalendarDay{ \n " +
                " AD - " + ad.toString() + " \n" +
                " BS - " + bs.toString() + " \n" +
                " type - " + type + " \n" +
                " }";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ad.getYear());
        dest.writeInt(ad.getMonth());
        dest.writeInt(ad.getDay());
        dest.writeInt(type);
    }

    public void subtractDay(int day) {
        ad.subtractDay(day);
        bs.subtractDay(day);
    }

    public void addDay(int day) {
        ad.addDays(day);
        bs.addDays(day);
    }

    public void subtractMonth(int month) {
        ad.subtractMonth(month);
        bs.subtractMonth(month);
    }

    public void addMonth(int month) {
        ad.addMonths(month);
        bs.addMonths(month);
    }

    public void subtractYear(int year) {
        ad.subtractYear(year);
        bs.subtractYear(year);
    }

    public void addYear(int year) {
        ad.addYear(year);
        bs.addYear(year);
    }

    private String convertToString(BaseCalendar calendar, int value){
        return calendar.getType() == BaseCalendar.AD  ? String.valueOf(value) : BSCalendar.toNepali(value);
    }

    @Override
    public Object clone(){
        try {
            return super.clone();
        } catch (Exception e) {
            return new CalendarDay(ad, bs, type);
        }
    }

    public int getNoOfWeek() {
        return getActiveCalendar().countWeek();
    }

    public int getMaxDayInMonth() {
        return getActiveCalendar().getMaximumDaysInMonth();
    }
}
