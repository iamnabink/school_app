package com.swipecrafts.school.utils.animators;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Interpolator;

/**
 * Created by Madhusudan Sapkota on 5/18/2018.
 */

public class FadeInLeftAnimator extends BaseItemAnimator {

    public FadeInLeftAnimator() {
    }

    public FadeInLeftAnimator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    @Override protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
        ViewCompat.animate(holder.itemView)
                .translationX(-holder.itemView.getRootView().getWidth() * .25f)
                .alpha(0)
                .setDuration(getRemoveDuration())
                .setInterpolator(mInterpolator)
                .setListener(new DefaultRemoveVpaListener(holder))
                .setStartDelay(getRemoveDelay(holder))
                .start();
    }

    @Override protected void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
        ViewCompat.setTranslationX(holder.itemView, -holder.itemView.getRootView().getWidth() * .25f);
        ViewCompat.setAlpha(holder.itemView, 0);
    }

    @Override protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
        ViewCompat.animate(holder.itemView)
                .translationX(0)
                .alpha(1)
                .setDuration(getAddDuration())
                .setInterpolator(mInterpolator)
                .setListener(new DefaultAddVpaListener(holder))
                .setStartDelay(getAddDelay(holder))
                .start();
    }
}
