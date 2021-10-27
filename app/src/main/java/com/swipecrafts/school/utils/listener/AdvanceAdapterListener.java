package com.swipecrafts.school.utils.listener;

import android.widget.ImageView;

/**
 * Created by Madhusudan Sapkota on 3/4/2018.
 */

public interface AdvanceAdapterListener<A> extends AdapterListener<A> {

    // returns the local url of file/image
    void performImageDownloadTask(A item, ImageView imageView);
}
