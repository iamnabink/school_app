package com.swipecrafts.school.utils.listener;

/**
 * Created by Madhusudan Sapkota on 3/23/2018.
 */

public interface DataLoader<D> {
    void onLoaded(D data);
    void onProgress(double percent);
    void onFailed(String message);
}
