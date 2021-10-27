package com.swipecrafts.library.calendar.decorator;

import com.swipecrafts.library.calendar.DayViewDecorator;
import com.swipecrafts.library.calendar.DayViewFacade;
import com.swipecrafts.library.calendar.core.ADCalendar;
import com.swipecrafts.library.calendar.core.CalendarDay;
import com.swipecrafts.library.calendar.spans.DotSpan;

import java.util.Collection;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private int color;
    private Collection<Long> events;

    public EventDecorator(int color, Collection<Long> dates) {
        this.color = color;
        this.events = dates;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        ADCalendar calendar = day.getADCalendar();
        long dayHash = ((calendar.getYear() * 1000000) + ((calendar.getMonth()) * 1000) + (calendar.getDay() * 10));
        boolean hasEvent = events.contains(dayHash);
        return hasEvent;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setEventColor(color);
        view.addSpan(new DotSpan(10, color));
    }
}
