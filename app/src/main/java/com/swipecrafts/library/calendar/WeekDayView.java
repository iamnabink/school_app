package com.swipecrafts.library.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.swipecrafts.library.calendar.core.CalendarDay;
import com.swipecrafts.library.calendar.format.WeekDayFormatter;

/**
 * Display a day of the week
 */
@Experimental
@SuppressLint({"ViewConstructor", "AppCompatCustomView"})
class WeekDayView extends TextView {

    private WeekDayFormatter formatter = WeekDayFormatter.DEFAULT;
    private int dayOfWeek;

    public WeekDayView(Context context, int dayOfWeek) {
        super(context);

        if (dayOfWeek == 6) setTextColor(Color.RED);

        setGravity(Gravity.CENTER);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setTextAlignment(TEXT_ALIGNMENT_CENTER);
        }

        setDayOfWeek(dayOfWeek);
    }

    public void setWeekDayFormatter(WeekDayFormatter formatter) {
        this.formatter = formatter == null ? WeekDayFormatter.DEFAULT : formatter;
        setDayOfWeek(dayOfWeek);
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        setText(formatter.format(dayOfWeek, true));
    }

    public void setDayOfWeek(CalendarDay calendar) {
        setDayOfWeek(CalendarUtils.getDayOfWeek(calendar));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, (int)((right - left) * 0.30));
        super.onLayout(changed, left, top, right, bottom);
    }
}
