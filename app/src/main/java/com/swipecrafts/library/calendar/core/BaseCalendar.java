package com.swipecrafts.library.calendar.core;

import android.os.SystemClock;
import android.support.annotation.IntRange;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Madhusudan Sapkota on 3/8/2018.
 */

public abstract class BaseCalendar {

    public static final int AD = 0;
    public static final int BS = 1;
    protected int year;
    protected int month;
    protected int day;
    protected int hour;
    protected int minute;
    protected int second;

    BaseCalendar(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    BaseCalendar(int year, int month, int day) {
        this(year, month, day, 0, 0, 0);
    }

    BaseCalendar(BaseCalendar calendar) {
        this(calendar.getYear(), calendar.getMonth(), calendar.getDay(), calendar.getHour(), calendar.getMinute(), calendar.getSecond());
    }

    public static BaseCalendar from(BaseCalendar calendar, @CalendarType int type) {
        if (calendar.getType() == type) return calendar.clone();
        else return from(calendar.getTime(), type);
    }

    public static BaseCalendar from(Date date, @CalendarType int type) {
        if (type == AD) {
            ADCalendar c = new ADCalendar();
            c.setTime(date);
            return c;
        } else if (type == BS) {
            BSCalendar c = new BSCalendar();
            c.setTime(date);
            return c;
        }

        throw new IllegalArgumentException("Unsupported calendar type!");
    }

    public static BaseCalendar from(Calendar calendar, @CalendarType int type) {
        return from(calendar.getTime(), type);
    }

    public static BaseCalendar getInstance(@CalendarType int type) {
        return type == BS ? BSCalendar.today() : ADCalendar.today();
    }

    public static BaseCalendar convert(BaseCalendar calendar, @CalendarType int type) {
        if (type == AD)
            return calendar.getType() == type ? calendar.clone() : convertToAD(calendar);
        else if (type == BS)
            return calendar.getType() == type ? calendar.clone() : convertToBS(calendar);
        throw new IllegalArgumentException("Calendar type not supported!");
    }

    private static ADCalendar convertToAD(BaseCalendar nepaliDate) {
        ADCalendar englishDate = new ADCalendar();
        englishDate.addDays(nepaliDate.getDaysSinceReferenceDate());
        return englishDate;
    }

    private static BSCalendar convertToBS(BaseCalendar englishDate) {
        BSCalendar nepaliDate = new BSCalendar();
        nepaliDate.addDays(englishDate.getDaysSinceReferenceDate());
        return nepaliDate;
    }

    // static methods
    public static int getDaysDifference(Date from, Date to) {
        long msDiff = from.getTime() - to.getTime();
        if (msDiff < 0) msDiff *= -1;
        return (int) TimeUnit.MILLISECONDS.toDays(msDiff);
    }

    public static int getDaysDifference(BaseCalendar from, BaseCalendar to) {
        BaseCalendar referenceDate;
        BaseCalendar baseDate;

        int i;
        if (from.before(to)) {
            i = 1;

            referenceDate = to.clone();
            baseDate = from.clone();
        } else {
            i = -1;

            baseDate = to.clone();
            referenceDate = from.clone();
        }

        int daysNumber = 0;
        while (baseDate.before(referenceDate)) {
            baseDate.addSingleDay();
            daysNumber++;
        }
        return daysNumber * i;
    }

    private static int hashCode(int year, int month, int day) {
        //Should produce hashes like "20150401"
        return (year * 10000) + (month * 100) + day;
    }

    public static void main(String[] args) {
        ADCalendar ad = new ADCalendar(2021, 8, 14);
        System.out.println("Start1: " + SystemClock.uptimeMillis());

        int days = ad.getDaysSinceReferenceDate();
        System.out.println("Days: " + days);
        System.out.println("Milisecond: " + days * (24 * 60 * 60 * 1000));
        System.out.println("End1: " + SystemClock.uptimeMillis());

        System.out.println("Start2: " + SystemClock.uptimeMillis());

        long days1 = ad.testDate();
        System.out.println("Days: " + days1 / (24 * 60 * 60 * 1000));
        System.out.println("Milisecond: " + days1);
        System.out.println("End2: " + SystemClock.uptimeMillis());

        Date date = new Date(100000L);
        date.getTime();
    }

    public abstract String[] days();

    protected abstract String[] daysOfWeeks();

    public abstract String[] daysOfWeeksShort();

    protected abstract String[] months();

    protected abstract String[] monthsShort();

    public abstract BaseCalendar getReferenceDate();

    public abstract int getMaximumDaysInMonth();

    public abstract int getMaximumDaysInMonth(int year, @IntRange(from = 1, to = 12) int month);

    public abstract int getMaximumDaysInYear();

    public abstract int getMaximumDaysInYear(int year);

    public abstract int getFirstDayOfWeek();

    public abstract boolean isThisMonth();

    public int getDayOfWeek() {
        int daysSinceReferenceDate = this.getDaysSinceReferenceDate();

        return daysSinceReferenceDate > 0 ?
                ((daysSinceReferenceDate % 7) + this.getFirstDayOfWeek()) % 7 :
                (((7 - ((daysSinceReferenceDate * -1) % 7)) % 7) + this.getFirstDayOfWeek()) % 7;
    }

    // reference date
    public int getDaysSinceReferenceDate() {
        return getDaysDifference(this.getReferenceDate(), this);
    }

    public Long testDate() {
        long time = 0;
        BaseCalendar fromTime = getReferenceDate();
        BaseCalendar toTime = this;

        time =
                (60 - fromTime.second) * 1000 +
                        (60 - fromTime.minute) * 60000 +
                        (24 - fromTime.hour) * 3600000L +
                        (fromTime.getMaximumDaysInMonth() - fromTime.day) * 86400000L;

        for (int i = (fromTime.month + 1); i <= 12; i++) {
            time += (getMaximumDaysInMonth(fromTime.year, i)) * 86400000L;
        }
        if (fromTime.year + 1 != toTime.year)
            for (int i = (fromTime.year + 1); i <= toTime.year; i++) {
                time += fromTime.getMaximumDaysInYear(i) * 86400000L;
            }

        for (int i = 1; i <= (toTime.month - 1); i++) {
            time += getMaximumDaysInMonth(toTime.year, i) * 86400000L;
        }
        time +=
                (toTime.day) * 86400000L +
                        (toTime.hour) * 3600000L +
                        (toTime.minute) * 60000 +
                        (toTime.second) * 1000;

        return time;
    }

    public void addSingleDay() {
        this.day++;
        if (this.day > this.getMaximumDaysInMonth()) {
            this.day = 1;
            this.month++;
            if (this.month > 12) {
                this.month = 1;
                this.year++;
            }
        }
    }

    public void addDays(int days) {
        if (days > 0) {
            for (int counter = 0; counter < days; counter++) {
                this.addSingleDay();
            }
            return;
        }

        // handel if the month is negative
        if (days < 0) subtractDay(days);
    }

    public void addSingleMonth() {
        this.month++;
        if (month > 12) {
            this.year++;
            this.month = 1;
        }
    }

    public void addMonths(int month) {
        if (month > 0) {
            while (month > 0) {
                this.addSingleMonth();
                month--;
            }
            return;
        }

        // handel if the month is negative
        if (month < 0) subtractMonth(month);

    }

    public void addYear(int year) {
        // handel if the month is negative
        if (month < 0) {
            subtractYear(year);
            return;
        }

        this.year += year;
    }

    public void subtractSingleDay() {
        this.day--;
        if (this.day < 1) {
            this.month--;
            if (this.month < 1) {
                this.year--;
                this.month = 12;
            }
            this.day = this.getMaximumDaysInMonth();
        }
    }

    public void subtractDay(int day) {
        // handel if the days is negative
        if (day < 0) day = Math.abs(day);

        while (day != 0) {
            day--;
            this.subtractSingleDay();
        }
    }

    public void subtractSingleMonth() {
        this.month--;
        if (this.month < 1) {
            this.year--;
            this.month = 12;
        }
    }

    public void subtractMonth(int month) {
        // handel if the month is negative
        if (month < 0) month = Math.abs(month);

        while (month != 0) {
            month--;
            this.subtractSingleMonth();
        }
    }

    public void subtractYear(int year) {
        if (this.year < year) return;
        // handel if the month is negative
        if (month < 0) month = Math.abs(month);
        this.year -= year;
    }

    // date mappers
    public boolean equals(BaseCalendar date) {
        return date != null &&
                this.day == date.getDay() &&
                this.month == date.getMonth() &&
                this.year == date.getYear() &&
                this.hour == date.hour &&
                this.minute == date.minute;
    }

    public boolean before(BaseCalendar baseCalendar) {
        return this.year < baseCalendar.getYear() ||
                (this.year <= baseCalendar.getYear() &&
                        (this.month < baseCalendar.getMonth() ||
                                (this.month <= baseCalendar.getMonth() &&
                                        (this.day < baseCalendar.getDay() ||
                                                (this.day <= baseCalendar.getDay() &&
                                                        (this.hour < baseCalendar.getHour() ||
                                                                (this.hour <= baseCalendar.getHour() &&
                                                                        this.minute < baseCalendar.getMinute()
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );
    }

    public boolean after(BaseCalendar baseCalendar) {
        return this.year > baseCalendar.getYear() ||
                (this.year >= baseCalendar.getYear() &&
                        (this.month > baseCalendar.getMonth() ||
                                (this.month >= baseCalendar.getMonth() &&
                                        (this.day > baseCalendar.getDay() ||
                                                (this.day >= baseCalendar.getDay() &&
                                                        (this.hour > baseCalendar.getHour() ||
                                                                (this.hour >= baseCalendar.getHour() &&
                                                                        this.minute > baseCalendar.getMinute()
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );
    }

    // getter setters
    public void setTime(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setTime(int year, int month, int day, int hour, int second, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int countWeek() {
        BaseCalendar cal = this.clone();
        CalendarUtils.setToFirstDay(cal);

        int count = cal.getDayOfWeek() == 0 ? 0 : 1;
        int currentMonth = cal.getMonth();
        int exitMonth = currentMonth == 12 ? 1 : currentMonth + 1;
        while (true) {
            if (cal.getMonth() == exitMonth) break;
            if (cal.getDayOfWeek() == 0) count++;
            cal.addSingleDay();
        }
        return count;
    }

    public int countPassesWeek() {
        BaseCalendar calendar = this.clone();
        CalendarUtils.setToFirstDay(calendar);

        int count = 0;
        int weekDay = 0;
        int exitDay = this.day;
        while (true) {
            if (calendar.getDay() == day) break;
            weekDay = calendar.getDayOfWeek();
            if (weekDay == 6) count++;
            calendar.addSingleDay();
        }

        return count;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public Date getTime() {
        if (getType() == AD) {
            return new GregorianCalendar(year, month - 1, day, hour, minute, second).getTime();
        } else {
            ADCalendar ad = convertToAD(new BSCalendar(year, month, day));
            return new GregorianCalendar(ad.getYear(), ad.getMonth() - 1, ad.getDay(), ad.getHour(), ad.getMinute(), ad.getSecond()).getTime();
        }
    }

    public void setTime(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);

        if (getType() == AD) {
            setTime(year, month, day, hour, min, sec);
        } else {
            BSCalendar bs = convertToBS(new ADCalendar(year, month, day, hour, min, sec));
            setTime(bs.getYear(), bs.getMonth(), bs.getDay(), bs.getHour(), bs.getMinute(), bs.getSecond());
        }
    }

    public int hashCode() {
        return (year * 10000) + (month * 100) + day;
    }

    public long toLong() {
        return (long) (((year * 10000) + (month * 100)) + (day * 10));
    }

    @CalendarType
    public abstract int getType();

    @Override
    public BaseCalendar clone() {
        try {
            return (BaseCalendar) super.clone();
        } catch (Exception e) {
            return getType() == AD ? new ADCalendar(year, month, day) : getType() == BS ? new BSCalendar(year, month, day) : null;
        }
    }

    @Override
    public String toString() {
        return "BaseCalendar{" + year + "-" + month + "-" + day + "}";
    }
}