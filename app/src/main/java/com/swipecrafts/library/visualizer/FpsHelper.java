package com.swipecrafts.library.visualizer;

import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Madhusudan Sapkota on 7/4/2018.
 */
class FpsHelper {
    private static final String TAG = "TAG";

    private static final int DEFAULT_CALIBRATION = 2;
    private static final long DEFAULT_TOLERATE_TIME = 2000L;

    private long mFrameGap = 1000 / 24;
    private long mLastTrackTime = -1L;
    private long mAwaitTime = 0;
    private long mSkippedFrame = 0;
    private long mLastSampleTime = -1L;
    private int mFps = 0;

    void start() {
        long curTime = SystemClock.elapsedRealtime();
        long interval = curTime - mLastTrackTime;
        long lastAwaitTime = mAwaitTime;
        mAwaitTime = mFrameGap;
        if (interval < DEFAULT_TOLERATE_TIME) {
            if (mLastTrackTime >= 0L) {
                mAwaitTime -= (((int) interval) - lastAwaitTime);
            }
        } else {
            mFps = 0;
            mLastSampleTime = curTime;
        }
        mAwaitTime -= DEFAULT_CALIBRATION;
        mLastTrackTime = curTime;
        if (mLastSampleTime == -1L) {
            mLastSampleTime = curTime;
        }
    }

    void end() {
        long curTime = SystemClock.elapsedRealtime();
        mAwaitTime -= (int) (curTime - mLastTrackTime);
        if (mAwaitTime < 0) {
            mSkippedFrame = -mAwaitTime / mFrameGap + 1;
            mAwaitTime += mSkippedFrame * mFrameGap;
            Log.d(FpsHelper.TAG, "skipped frame: $mSkippedFrame, await: $mAwaitTime");
        } else {
            mSkippedFrame = 0;
        }
        mLastTrackTime = curTime;
        mFps++;
        if (curTime - mLastSampleTime > 1000L) {
            Log.d(TAG, "current fps: ${(1000.0 / (curTime - mLastSampleTime) * mFps).toInt()}");
            mFps = 0;
            mLastSampleTime = curTime;
        }
    }

    long nextDelayTime() {
        return mAwaitTime;
    }
}
