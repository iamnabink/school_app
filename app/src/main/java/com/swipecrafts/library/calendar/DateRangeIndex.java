package com.swipecrafts.library.calendar;

import com.swipecrafts.library.calendar.core.CalendarDay;

/**
 * Use math to calculate first days of months by postion from a minium date
 */
interface DateRangeIndex {

    int getCount();

    int indexOf(CalendarDay day);

    CalendarDay getItem(int position);
}
