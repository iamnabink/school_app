package com.swipecrafts.school.utils.listener;

import android.widget.ImageView;

/**
 * Created by Madhusudan Sapkota on 3/16/2018.
 */

public interface ImageLoader<M> {
    void loadImage(ImageView imageView, M model);
}
