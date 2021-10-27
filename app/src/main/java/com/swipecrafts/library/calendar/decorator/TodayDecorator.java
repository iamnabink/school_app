package com.swipecrafts.library.calendar.decorator;

import android.graphics.drawable.Drawable;

import com.swipecrafts.library.calendar.DayView;
import com.swipecrafts.library.calendar.DayViewDecorator;
import com.swipecrafts.library.calendar.DayViewFacade;
import com.swipecrafts.library.calendar.core.CalendarDay;

/**
 * Created by Madhusudan Sapkota on 5/16/2018.
 */
public class TodayDecorator implements DayViewDecorator {

    private int color;
    private CalendarDay today;
    private Drawable drawable;

    public TodayDecorator(int color) {
        this.color = color;
        this.today = CalendarDay.today();
        this.drawable = DayView.generateCircleDrawable(this.color);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(today);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
    }
}
