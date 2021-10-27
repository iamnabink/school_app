package com.swipecrafts.library.calendar.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.swipecrafts.library.calendar.core.CalendarDay;

/**
 * Format using a {@linkplain DateFormat} instance.
 */
public class DateFormatTitleFormatter implements TitleFormatter {

    private final DateFormat dateFormat;

    /**
     * Format using "LLLL yyyy" for formatting
     */
    public DateFormatTitleFormatter() {
        this.dateFormat = new SimpleDateFormat(
                "LLLL yyyy", Locale.getDefault()
        );
    }

    /**
     * Format using a specified {@linkplain DateFormat}
     *
     * @param format the format to use
     */
    public DateFormatTitleFormatter(DateFormat format) {
        this.dateFormat = format;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CharSequence format(CalendarDay day) {
        return day.getDisplayYear(true) +" " + day.getDisplayMonth(true, true) +
                " : "+ day.getDisplayYear(false)+" "+ day.getDisplayMonth(true, false);
    }
}
