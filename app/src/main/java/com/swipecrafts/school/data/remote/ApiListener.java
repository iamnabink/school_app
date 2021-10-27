package com.swipecrafts.school.data.remote;

/**
 * Created by Madhusudan Sapkota on 2/23/2018.
 */

public interface ApiListener<A> {
    void onSuccess(A response);
    void onFailed(String message, int code);
}
