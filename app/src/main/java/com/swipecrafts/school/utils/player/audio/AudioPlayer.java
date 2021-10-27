package com.swipecrafts.school.utils.player.audio;

import android.annotation.TargetApi;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.swipecrafts.school.R;
import com.swipecrafts.school.utils.player.MediaPlayer;
import com.swipecrafts.school.utils.player.MediaPlayerState;

import java.io.File;

/**
 * Created by Madhusudan Sapkota on 6/29/2018.
 */
public class AudioPlayer extends MediaPlayer implements Player.EventListener, MediaPlayer.ProgressUpdateListener {
    private static final String TAG = MediaPlayer.class.getSimpleName();
    private static final int MAX_CACHE_SIZE = 250_000_000;
    private static final String DC_FEED_CACHE_DIR = "digital-class-feed-cache";

    private final Context mContext;
    private final SimpleExoPlayer mMediaPlayer;
    private final Handler mainHandler;
    private final DefaultExtractorsFactory extractorsFactory;

    private AudioSource mediaSource;
    private boolean mIsStreaming;
    private int mMediaPlayerState;
    private boolean isUpdatingProgress;
    private Runnable mProgressUpdater;
    private MutableLiveData<Integer> mSessionId = new MutableLiveData<>();

    public AudioPlayer(Context context, MediaPlayerListener mediaPlayerListener, ProgressUpdateListener progressUpdateListener) {
        super(mediaPlayerListener, progressUpdateListener);
        mContext = context;

        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        DefaultLoadControl loadControl = new DefaultLoadControl();
        DefaultRenderersFactory rendererFactory = new DefaultRenderersFactory(context);
        mMediaPlayer = ExoPlayerFactory.newSimpleInstance(rendererFactory, trackSelector, loadControl);
        mMediaPlayer.addAudioDebugListener(new AudioRendererEventListener() {

            @Override
            public void onAudioEnabled(DecoderCounters counters) {

            }

            @Override
            public void onAudioSessionId(int audioSessionId) {
                mSessionId.postValue(audioSessionId);
            }

            @Override
            public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

            }

            @Override
            public void onAudioInputFormatChanged(Format format) {

            }

            @Override
            public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {

            }

            @Override
            public void onAudioDisabled(DecoderCounters counters) {

            }
        });

        extractorsFactory = new DefaultExtractorsFactory();
        mainHandler = new Handler();

        mMediaPlayer.addListener(this);
        mMediaPlayerState = MediaPlayerState.STATE_IDLE;
        mProgressUpdater = new ProgressUpdater();
    }

    private static CacheDataSourceFactory getCacheDataSource(File cacheDir, String userAgent) {
        Cache cache = new SimpleCache(cacheDir, new LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE));
        DataSource.Factory upstream = new DefaultHttpDataSourceFactory(userAgent, null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
        return new CacheDataSourceFactory(cache, upstream,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
                CacheDataSource.DEFAULT_MAX_CACHE_FILE_SIZE);
    }

    @Override
    public int getState() {
        return mMediaPlayerState;
    }

    @Override
    public long getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public long getBufferedPosition() {
        return mMediaPlayer.getBufferedPosition();
    }

    @Override
    public boolean isStreaming() {
        return mIsStreaming;
    }

    @Override
    public void loadMediaSource(AudioSource source) {
        mediaSource = source;
    }

    @Override
    public void startPlayback(boolean playImmediately) {
        if (mediaSource.getProgress() > -1) {
            mMediaPlayer.seekTo(mediaSource.getProgress());
        } else {
            mMediaPlayer.seekTo(0);
        }

        MediaSource media = buildMediaSource();
        mMediaPlayer.addListener(this);
        mMediaPlayer.prepare(media);
        mMediaPlayer.setPlayWhenReady(playImmediately);
    }

    @Override
    public void resumePlayback() {
        mMediaPlayer.setPlayWhenReady(true);
    }

    @Override
    public void pausePlayback() {
        mMediaPlayer.setPlayWhenReady(false);
    }

    @Override
    public void stopPlayback() {
        mMediaPlayer.stop();
        mIsStreaming = false;
        mediaSource = null;
    }

    @Override
    public void seekTo(long position) {
        mMediaPlayer.seekTo(position);
    }

    @Override
    public void tearDown() {
        Log.e(TAG, "Tearing down");
        super.tearDown();

        stopProgressUpdater();
        mMediaPlayer.release();
        mMediaPlayer.removeListener(this);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        String playbackStateStr;

        switch (playbackState) {
            case Player.STATE_BUFFERING:
                mMediaPlayerState = MediaPlayerState.STATE_CONNECTING;
                playbackStateStr = "Buffering";
                break;
            case Player.STATE_ENDED:
                mMediaPlayerState = MediaPlayerState.STATE_ENDED;
                playbackStateStr = "Ended";
                break;
            case Player.STATE_IDLE:
                mMediaPlayerState = MediaPlayerState.STATE_IDLE;
                playbackStateStr = "Idle";
                break;
            case Player.STATE_READY:
                mMediaPlayerState = playWhenReady ? MediaPlayerState.STATE_PLAYING : MediaPlayerState.STATE_PAUSED;
                playbackStateStr = "Ready";

                if (playWhenReady) {
                    startProgressUpdater();
                } else {
                    stopProgressUpdater();
                }
                break;
            default:
                mMediaPlayerState = MediaPlayerState.STATE_IDLE;
                playbackStateStr = "Unknown";
                break;
        }
        mMediaPlayerListener.onStateChanged(mMediaPlayerState);
        Log.e(TAG, String.format("ExoPlayer state changed: %s, Play When Ready: %s", playbackStateStr, String.valueOf(playWhenReady)));
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.w(TAG, "Player error encountered", error);
        stopPlayback();
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onProgressUpdate(long progress, long bufferedProgress, long duration) {
        mProgressUpdateListener.onProgressUpdate(progress, isStreaming() ? bufferedProgress : duration, duration);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void setPlaybackSpeed(float speed) {
        PlaybackParameters playbackParams = new PlaybackParameters(speed, 1f);
        mMediaPlayer.setPlaybackParameters(playbackParams);
    }

    @Override
    public LiveData<Integer> getAudioSessionId() {
        Log.e("Audio SessionID", mSessionId.getValue()+"");
        return mSessionId;
    }

    @Override
    public int getSessionId(){
        return this.mSessionId.getValue() == null || this.mSessionId.getValue() == 0 ? mMediaPlayer.getAudioSessionId() : this.mSessionId.getValue();
    }

    private MediaSource buildMediaSource() {
        Uri uri = null;
        DataSource.Factory dataSourceFactory = null;

        // return the uri to play
        switch (mediaSource.getDownloadStatus()) {
            case DOWNLOADED:
                uri = Uri.parse(mediaSource.getRemoteUrl());
                dataSourceFactory = new FileDataSourceFactory();
                mIsStreaming = false;
                break;
            case DOWNLOADING:
            case NOT_DOWNLOADED:
                uri = Uri.parse(mediaSource.getRemoteUrl());
                dataSourceFactory = getCacheDataSource(new File(mContext.getCacheDir(), DC_FEED_CACHE_DIR), mContext.getString(R.string.user_agent));
                mIsStreaming = true;
                break;
        }

        ExtractorMediaSource.Factory extractorMediaSourceFactory = new ExtractorMediaSource
                .Factory(dataSourceFactory)
                .setExtractorsFactory(extractorsFactory);

        if (uri != null) {
            Log.e(TAG, "Playing from URI " + uri);
            return extractorMediaSourceFactory.createMediaSource(uri, mainHandler, null);
        }
        throw new IllegalStateException("Unable to build media source");
    }

    private void startProgressUpdater() {

        if (!isUpdatingProgress) {
            mProgressUpdater.run();
            isUpdatingProgress = true;
        }
    }

    private void stopProgressUpdater() {

        if (isUpdatingProgress) {
            mainHandler.removeCallbacks(mProgressUpdater);
            isUpdatingProgress = false;
        }
    }

    // spins the album art like a record
    private class ProgressUpdater implements Runnable {

        private static final int TIME_UPDATE_MS = 16;

        @Override
        public void run() {
            long progress = mMediaPlayer.getCurrentPosition();
            long duration = mMediaPlayer.getDuration();
            mProgressUpdateListener.onProgressUpdate(progress, 0, duration);
            mainHandler.postDelayed(mProgressUpdater, TIME_UPDATE_MS);
        }
    }
}
