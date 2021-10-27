package com.swipecrafts.school.utils.player;

import android.arch.lifecycle.LiveData;

import com.swipecrafts.school.utils.player.audio.AudioSource;

/**
 * Created by Madhusudan Sapkota on 6/29/2018.
 */
public abstract class MediaPlayer {

    protected MediaPlayerListener mMediaPlayerListener;
    protected ProgressUpdateListener mProgressUpdateListener;

    public MediaPlayer(MediaPlayerListener mediaPlayerListener, ProgressUpdateListener progressUpdateListener) {
        mMediaPlayerListener = mediaPlayerListener;
        mProgressUpdateListener = progressUpdateListener;
    }

    public void tearDown() {
        mMediaPlayerListener = null;
        mProgressUpdateListener = null;
    }

    public abstract void loadMediaSource(AudioSource source);

    public abstract void startPlayback(boolean playImmediately);

    public abstract void resumePlayback();

    public abstract void pausePlayback();

    public abstract void stopPlayback();

    public abstract void seekTo(long position);

    public abstract boolean isStreaming();

    public abstract int getState();

    public abstract long getCurrentPosition();

    public abstract long getDuration();

    public abstract long getBufferedPosition();

    public abstract void setPlaybackSpeed(float speed);

    public abstract LiveData<Integer> getAudioSessionId();

    public abstract int getSessionId();

    public interface MediaPlayerListener {
        void onStateChanged(int state);
    }

    public interface ProgressUpdateListener {
        void onProgressUpdate(long progress, long bufferedProgress, long duration);
    }
}
