package com.swipecrafts.school.utils.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.swipecrafts.school.utils.player.MediaPlayer;
import com.swipecrafts.school.utils.player.audio.AudioSource;

/**
 * Created by Madhusudan Sapkota on 6/29/2018.
 */
public class AudioPlayerService extends Service implements MediaPlayer.MediaPlayerListener, MediaPlayer.ProgressUpdateListener {

    public static final String PARAM_PLAYLIST           = "playlist";
    public static final String PARAM_AUDIO_ID           = "episodeId";
    public static final String PARAM_SEEK_MS            = "seekMs";
    public static final String PARAM_CAST_DEVICE        = "castDevice";
    public static final String PARAM_PLAYBACK_SPEED     = "playbackSpeed";

    // media related objects
    private MediaPlayer mMediaPlayer;
    private MediaSession mMediaSession;
    private AudioManager mAudioManager;
    private WifiManager.WifiLock mWifiLock;
    private PlaybackState mPlaybackState;
    private int mMediaPlayerState;
    private AudioSource mCurrentEpisode;
    private boolean mPlayingBeforeFocusChange;
    private boolean mReceiverRegistered;
    private int mStreamVolume = -1;
    private boolean mServiceBound = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStateChanged(int state) {

    }

    @Override
    public void onProgressUpdate(long progress, long bufferedProgress, long duration) {

    }
}
