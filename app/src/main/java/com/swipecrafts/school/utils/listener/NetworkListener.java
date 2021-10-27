package com.swipecrafts.school.utils.listener;

import android.net.NetworkInfo;

/**
 * Created by Madhusudan Sapkota on 2/18/2018.
 */

public interface NetworkListener {
    void onNetworkAvailable(NetworkInfo type);
    void onNetworkUnavailable();
}