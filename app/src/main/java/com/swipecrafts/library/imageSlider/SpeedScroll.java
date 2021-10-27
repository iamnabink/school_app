package com.swipecrafts.library.imageSlider;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by Madhusudan Sapkota on 6/13/2018.
 */
public class SpeedScroll extends Scroller {
    private int mDuration = 7000;
    private double mScrollFactor = 1;

    public SpeedScroll(Context context) {
        super(context);
    }

    public SpeedScroll(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public SpeedScroll(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }


    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        this.mDuration = duration;
        super.startScroll(startX, startY, dx, dy, (int) (mDuration * mScrollFactor));
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, (int) (mDuration * mScrollFactor));
    }

    public void setScrollDurationFactor(double scrollDurationFactor) {
        this.mScrollFactor = scrollDurationFactor;
    }
}
