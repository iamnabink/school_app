package com.swipecrafts.library.calendar.format;

import com.swipecrafts.library.calendar.CalendarUtils;
import com.swipecrafts.library.calendar.MaterialCalendarView;

/**
 * Supply labels for a given day of the week
 */
public interface WeekDayFormatter {
    /**
     * Convert a given day of the week into a label
     *
     * @param dayOfWeek the day of the week as returned by {@linkplain java.util.Calendar#get(int)} for {@linkplain java.util.Calendar#DAY_OF_YEAR}
     * @param isActive
     * @return a label for the day of week
     */
    CharSequence format(int dayOfWeek, boolean isActive);

    /**
     * Default implementation used by {@linkplain MaterialCalendarView}
     */
    public static final WeekDayFormatter DEFAULT = new CalendarWeekDayFormatter(CalendarUtils.getInstance());
}
