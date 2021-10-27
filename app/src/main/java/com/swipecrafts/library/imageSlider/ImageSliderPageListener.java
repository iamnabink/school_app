package com.swipecrafts.library.imageSlider;

import android.support.v4.view.ViewPager;

/**
 * Created by Madhusudan Sapkota on 3/13/2018.
 */

public class ImageSliderPageListener implements ViewPager.OnPageChangeListener {
    private SliderView mViewPager;
    private int mCurrentPosition;
    private int mScrollState;

    public ImageSliderPageListener(final SliderView viewPager) {
        mViewPager = viewPager;
    }

    @Override
    public void onPageSelected(final int position) {
        mCurrentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
        mScrollState = state;
        handleScrollState(state);
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
    }

    private void handleScrollState(final int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING){
            mViewPager.setAutoImageLooping(false);
        }else {
            mViewPager.setAutoImageLooping(true);
        }

        if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (!isScrollStateSettling()) {
                handleSetNextItem();
            }
        }
    }

    private boolean isScrollStateSettling() {
        return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
    }

    private void handleSetNextItem() {
        if (mCurrentPosition == 0) {
            mViewPager.previousImage();
        }
    }
}
