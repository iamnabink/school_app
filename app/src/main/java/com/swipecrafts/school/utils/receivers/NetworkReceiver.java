package com.swipecrafts.school.utils.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.utils.listener.NetworkListener;

/**
 * Created by Madhusudan Sapkota on 2/18/2018.
 */

public class NetworkReceiver extends BroadcastReceiver {

    private NetworkListener mListener;

    public NetworkReceiver() {
    }

    public NetworkReceiver(NetworkListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(Utils.isOnline(context))
            mListener.onNetworkAvailable(Utils.getNetworkType(context));
        else
            mListener.onNetworkUnavailable();
    }
}
