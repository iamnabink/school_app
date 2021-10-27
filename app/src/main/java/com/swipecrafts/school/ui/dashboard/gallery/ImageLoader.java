package com.swipecrafts.school.ui.dashboard.gallery;

import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by Madhusudan Sapkota on 3/16/2018.
 */

public interface ImageLoader{
    void loadImage(ImageView imageView, String url, ProgressBar progressBar);
}