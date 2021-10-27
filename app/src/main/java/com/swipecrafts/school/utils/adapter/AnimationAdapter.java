package com.swipecrafts.school.utils.adapter;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.swipecrafts.school.utils.ViewHelper;

/**
 * Created by Madhusudan Sapkota on 4/3/2018.
 */
public abstract class AnimationAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private int mDuration = 700;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mLastPosition = -1;

    private boolean isFirstOnly = true;

    public AnimationAdapter() {
    }

    @Override public void onBindViewHolder(@NonNull VH holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        if (!isFirstOnly || adapterPosition > mLastPosition) {
            for (Animator anim : getAnimators(holder.itemView)) {
                anim.setDuration(mDuration).start();
                anim.setInterpolator(mInterpolator);
            }
            mLastPosition = adapterPosition;
        } else {
            ViewHelper.clear(holder.itemView);
        }
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void setStartPosition(int start) {
        mLastPosition = start;
    }

    protected abstract Animator[] getAnimators(View view);

    public void setFirstOnly(boolean firstOnly) {
        isFirstOnly = firstOnly;
    }
}