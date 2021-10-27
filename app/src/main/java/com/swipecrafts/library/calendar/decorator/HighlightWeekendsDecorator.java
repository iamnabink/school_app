package com.swipecrafts.library.calendar.decorator;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.swipecrafts.library.calendar.DayViewDecorator;
import com.swipecrafts.library.calendar.DayViewFacade;
import com.swipecrafts.library.calendar.core.CalendarDay;

/**
 * Highlight Saturdays and Sundays with a background
 */
public class HighlightWeekendsDecorator implements DayViewDecorator {

    private final CalendarDay calendar =  CalendarDay.today();
    private final Drawable highlightDrawable;
    private static final int color = Color.parseColor("#228BC34A");

    public HighlightWeekendsDecorator() {
        highlightDrawable = new ColorDrawable(color);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.getActiveCalendar().getDayOfWeek();
        return weekDay == 6;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(highlightDrawable);
    }
}
