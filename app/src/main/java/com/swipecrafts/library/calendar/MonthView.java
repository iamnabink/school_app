package com.swipecrafts.library.calendar;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.swipecrafts.library.calendar.core.CalendarDay;

import java.util.Collection;

/**
 * Display a month of {@linkplain DayView}s and
 * seven {@linkplain WeekDayView}s.
 */
@SuppressLint("ViewConstructor")
class MonthView extends CalendarPagerView {

    public MonthView(@NonNull MaterialCalendarView view, CalendarDay month, int firstDayOfWeek) {
        super(view, month, firstDayOfWeek);
    }

    @Override
    protected void buildDayViews(Collection<DayView> dayViews, CalendarDay calendar) {
        for (int r = 0; r < numOfRows; r++) {
            for (int i = 0; i < DEFAULT_DAYS_IN_WEEK; i++) {
                addDayView(dayViews, calendar);
            }
        }
    }

    public CalendarDay getMonth() {
        return getFirstViewDay();
    }

    @Override
    protected boolean isDayEnabled(CalendarDay day) {
        return day.getMonth() == getFirstViewDay().getMonth();
    }

    @Override
    protected int getRows() {
        return numOfRows + DAY_NAMES_ROW;
    }
}
