package com.swipecrafts.library.calendar.core;

/**
 * Created by Madhusudan Sapkota on 4/12/2018.
 */
public enum WeekDay {

    SUNDAY(0),
    MONDAY(1),
    TUESDAY(2),
    THURSDAY(3),
    WEDNESDAY(4),
    FRIDAY(5),
    SATURDAY(6);

    private int weekDay;

    WeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public int getWeekDay() {
        return weekDay;
    }
}
