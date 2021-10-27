package com.swipecrafts.school.data.remote;

/**
 * Created by Madhusudan Sapkota on 3/4/2018.
 */

public interface ApiFileListener<A> {
    void onFinish(A data);
    void onProgress(long percent);
    void onFailed(String message);
}
