package com.swipecrafts.library.calendar.format;

import java.util.Calendar;
import java.util.Locale;

import com.swipecrafts.library.calendar.CalendarUtils;
import com.swipecrafts.library.calendar.core.CalendarDay;

/**
 * Use a {@linkplain Calendar} to get week day labels.
 *
 * @see Calendar#getDisplayName(int, int, Locale)
 */
public class CalendarWeekDayFormatter implements WeekDayFormatter {

    private final CalendarDay calendar;

    /**
     * Format with a specific calendar
     *
     * @param calendar CalendarDay to retrieve formatting information from
     */
    public CalendarWeekDayFormatter(CalendarDay calendar) {
        this.calendar = calendar;
    }

    /**
     * Format with a default calendar
     */
    public CalendarWeekDayFormatter() {
        this(CalendarUtils.getInstance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CharSequence format(int dayOfWeek, boolean isActive) {
        return calendar.getDisplayWeekName(dayOfWeek, true, isActive);
    }
}
