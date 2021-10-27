package com.swipecrafts.library.calendar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckedTextView;

import com.swipecrafts.library.calendar.core.CalendarDay;
import com.swipecrafts.library.calendar.format.DayFormatter;
import com.swipecrafts.library.calendar.MaterialCalendarView.ShowOtherDates;

import java.util.List;

import static com.swipecrafts.library.calendar.MaterialCalendarView.showDecoratedDisabled;
import static com.swipecrafts.library.calendar.MaterialCalendarView.showOtherMonths;
import static com.swipecrafts.library.calendar.MaterialCalendarView.showOutOfRange;

/**
 * Display one day of a {@linkplain MaterialCalendarView}
 */
@SuppressLint("AppCompatCustomView")
public class DayView extends CheckedTextView {

    private final int fadeTime;
    private final Paint secondaryDayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final PointF secondaryDayPoint = new PointF();
    private final Rect secondaryDayRect = new Rect();
    private final Rect tempRect = new Rect();
    private final Rect circleDrawableRect = new Rect();
    private final boolean isWeekendDay;
    private Rect eventRect = new Rect();
    //    private DayFormatter formatter = DayFormatter.DEFAULT;
    private CalendarDay date;
    private int selectionColor = Color.GRAY;
    private Drawable customBackground = null;
    private Drawable selectionDrawable;
    private Drawable mCircleDrawable;
    private Drawable mEventDrawable;
    private boolean isInRange = true;
    private boolean isInMonth = true;
    private boolean isDecoratedDisabled = false;
    @ShowOtherDates
    private int showOtherDates = MaterialCalendarView.SHOW_DEFAULTS;

    private @ColorInt
    int textColor = Color.BLACK;
    private @ColorInt
    int selectionTextColor = Color.WHITE;

    public DayView(Context context, CalendarDay day) {
        super(context);

        fadeTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        setSelectionColor(this.selectionColor);

        this.isWeekendDay = day.getActiveCalendar().getDayOfWeek() == 6;
        if (isWeekendDay) this.textColor = Color.RED;

        setGravity(Gravity.CENTER);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setTextAlignment(TEXT_ALIGNMENT_CENTER);
        }

        setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        setDay(day);
    }

    private static Drawable generateBackground(int color, int fadeTime, Rect bounds) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.setExitFadeDuration(fadeTime);
        drawable.addState(new int[]{android.R.attr.state_checked}, generateCircleDrawable(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.addState(new int[]{android.R.attr.state_pressed}, generateRippleDrawable(color, bounds));
        } else {
            drawable.addState(new int[]{android.R.attr.state_pressed}, generateCircleDrawable(color));
        }

        drawable.addState(new int[]{}, generateCircleDrawable(Color.TRANSPARENT));

        return drawable;
    }

    public static Drawable generateCircleDrawable(final int color) {
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(color);
        return drawable;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable generateRippleDrawable(final int color, Rect bounds) {
        ColorStateList list = ColorStateList.valueOf(color);
        Drawable mask = generateCircleDrawable(Color.WHITE);
        RippleDrawable rippleDrawable = new RippleDrawable(list, null, mask);
//        API 21
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            rippleDrawable.setBounds(bounds);
        }

//        API 22. Technically harmless to leave on for API 21 and 23, but not worth risking for 23+
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            int center = (bounds.left + bounds.right) / 2;
            rippleDrawable.setHotspotBounds(center, bounds.top, center, bounds.bottom);
        }

        return rippleDrawable;
    }

    public void setDay(CalendarDay date) {
        this.date = date;
        setText(getLabel(true));
    }

    /**
     * Set the new label formatter and reformat the current label. This preserves current spans.
     *
     * @param formatter new label formatter
     */
    public void setDayFormatter(DayFormatter formatter) {
//        this.formatter = formatter == null ? DayFormatter.DEFAULT : formatter;
//        CharSequence currentLabel = getText();
//        Object[] spans = null;
//        if (currentLabel instanceof Spanned) {
//            spans = ((Spanned) currentLabel).getSpans(0, currentLabel.length(), Object.class);
//        }
//        SpannableString newLabel = new SpannableString(getLabel(true));
//        if (spans != null) {
//            for (Object span : spans) {
//                newLabel.setSpan(span, 0, newLabel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//        }
//        setText(newLabel);
    }

    @NonNull
    public String getLabel(boolean isActive) {
//        return formatter.format(date);
        return date.getDisplayDay(isActive);
    }

    public void setSelectionColor(int color) {
        this.selectionColor = color;
        regenerateBackground();
    }

    /**
     * @param drawable custom selection drawable
     */
    public void setSelectionDrawable(Drawable drawable) {
        if (drawable == null) {
            this.selectionDrawable = null;
        } else {
            this.selectionDrawable = drawable.getConstantState().newDrawable(getResources());
        }
        regenerateBackground();
    }

    /**
     * @param drawable background to draw behind everything else
     */
    public void setCustomBackground(Drawable drawable) {
        if (drawable == null) {
            this.customBackground = null;
        } else {
            this.customBackground = drawable.getConstantState().newDrawable(getResources());
        }
        invalidate();
    }

    public CalendarDay getDate() {
        return date;
    }

    private void setEnabled() {
        boolean enabled = isInMonth && isInRange && !isDecoratedDisabled;
        super.setEnabled(isInRange && !isDecoratedDisabled);

        boolean showOtherMonths = showOtherMonths(showOtherDates);
        boolean showOutOfRange = showOutOfRange(showOtherDates) || showOtherMonths;
        boolean showDecoratedDisabled = showDecoratedDisabled(showOtherDates);

        boolean shouldBeVisible = enabled;

        if (!isInMonth && showOtherMonths) {
            shouldBeVisible = true;
        }

        if (!isInRange && showOutOfRange) {
            shouldBeVisible |= isInMonth;
        }

        if (isDecoratedDisabled && showDecoratedDisabled) {
            shouldBeVisible |= isInMonth && isInRange;
        }

        if (!isInMonth && shouldBeVisible && !isWeekendDay) {
            setTextColor(getTextColors().getColorForState(new int[]{-android.R.attr.state_enabled}, Color.GRAY));
            secondaryDayPaint.setColor(Color.GRAY);
        } else {
            setTextColor(textColor);
            secondaryDayPaint.setColor(textColor);
        }

        setVisibility(shouldBeVisible ? View.VISIBLE : View.INVISIBLE);
    }

    protected void setupSelection(@ShowOtherDates int showOtherDates, boolean inRange, boolean inMonth) {
        this.showOtherDates = showOtherDates;
        this.isInMonth = inMonth;
        this.isInRange = inRange;
        setEnabled();
    }

    @Override
    public void setChecked(boolean checked) {
        if (secondaryDayPaint != null && isInMonth) {
            if (checked) {
                setTextColor(selectionTextColor);
                secondaryDayPaint.setColor(selectionTextColor);
            } else {
                setTextColor(textColor);
                secondaryDayPaint.setColor(textColor);
            }
        }
        super.setChecked(checked);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (customBackground != null) {
            customBackground.setBounds(tempRect);
            customBackground.setState(getDrawableState());
            customBackground.draw(canvas);
        }

        if (mEventDrawable != null) {
            mEventDrawable.setBounds(eventRect);
            mEventDrawable.draw(canvas);
        }

        mCircleDrawable.setBounds(circleDrawableRect);

        super.onDraw(canvas);

        canvas.drawText(getLabel(false), secondaryDayPoint.x, secondaryDayPoint.y, secondaryDayPaint);

    }

    private void regenerateBackground() {
        if (selectionDrawable != null) {
            setBackground(selectionDrawable);
        } else {
            mCircleDrawable = generateBackground(selectionColor, fadeTime, circleDrawableRect);
            setBackground(mCircleDrawable);
        }
    }

    /**
     * @param facade apply the facade to us
     */
    void applyFacade(DayViewFacade facade) {
        this.isDecoratedDisabled = facade.areDaysDisabled();

        if (facade.getBackgroundDrawable() != null) {
            this.selectionTextColor = Color.BLACK;
        }
        setEnabled();
        String label = getLabel(true);
        // Facade has spans
        List<DayViewFacade.Span> spans = facade.getSpans();
        if (!spans.isEmpty()) {
            mEventDrawable = generateCircleDrawable(facade.getEventColor());
//            SpannableString formattedLabel = new SpannableString(label);
//            for (DayViewFacade.Span span : spans) {
//                formattedLabel.setSpan(span.span, 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            setText(formattedLabel);
        }
        // Reset in case it was customized previously
        else {
            mEventDrawable = null;
//            setText(label);
        }

        setCustomBackground(facade.getBackgroundDrawable());
        setSelectionDrawable(facade.getSelectionDrawable());

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        calculateBounds(right - left, bottom - top);
        regenerateBackground();
    }

    private void calculateBounds(int width, int height) {
        final int radius = Math.min(height, width);
        final int offset = Math.abs(height - width) / 2;

        // Lollipop platform bug. Circle drawable offset needs to be half of normal offset
        final int circleOffset = Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP ? offset / 2 : offset;

        int right = 0, left = 0;
        int bottom = 0, top = 0;

        if (width >= height) {
            tempRect.set(offset + 5, 0 + 5, radius + offset - 5, height - 5);
            circleDrawableRect.set(circleOffset, 0, radius + circleOffset, height);
            right = radius + offset;
            left = offset;
            bottom = height;
            top = 0;
        } else {
            tempRect.set(0 + 5, offset + 5, width - 5, radius + offset - 5);
            circleDrawableRect.set(0, circleOffset, width, radius + circleOffset);

            right = width;
            left = 0;
            bottom = radius + offset;
            top = offset;
        }

        int angle = 285;
        int centerX = (right - left) / 2;
        int centerY = (bottom - top) / 2;
        int circleRadius = right / 2;
        double x = centerX + circleRadius * Math.cos(angle);
        double y = centerY + circleRadius * Math.sin(angle);

        secondaryDayPoint.x = (float) (left + x + 5);
        secondaryDayPoint.y = (float) (top + y - 5);

        String dayText = getLabel(false);
        secondaryDayPaint.getTextBounds(dayText, 0, dayText.length(), secondaryDayRect);


        int eventX = (int) (centerX + circleRadius * Math.cos(340));
        int eventY = (int) (centerY + circleRadius * Math.sin(340));
        eventRect.set(eventX -20, eventY - 20, eventX, eventY);

        // set the text size based on devices
        setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (width * 0.30));
        secondaryDayPaint.setTextSize((int) (width * 0.20));
    }
}
