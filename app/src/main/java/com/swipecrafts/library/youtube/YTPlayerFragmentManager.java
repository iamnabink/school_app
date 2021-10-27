package com.swipecrafts.library.youtube;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by Madhusudan Sapkota on 6/5/2018.
 */
public abstract class YTPlayerFragmentManager extends Fragment {
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    public static final String VIDEO_FRAME_TAG = "youtube_video_frame";
    private static final int HACK_ID_PREFIX = 12331293; //some random number
    private static YouTubePlayerSupportFragment youTubePlayerFragment;
    private static YouTubePlayer youTubePlayer;
    private boolean isFullScreen = false;

    public void preparePlayer(FrameLayout view, String videoKey, int adapterPosition) {
        Log.e("Youtube Player", "Preparing Youtube Player VideoKey: " + videoKey + " ID: " + view.getId());
        View container = view.findViewWithTag(VIDEO_FRAME_TAG);
        if (container != null) {
            container.setId(HACK_ID_PREFIX + adapterPosition);
        }
        if (TextUtils.isEmpty(videoKey)) {
            return;
        }
        if (!YouTubeIntents.isYouTubeInstalled(getContext()) || YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(getContext()) != YouTubeInitializationResult.SUCCESS) {
            Log.e("Youtube Video", "Youtube is not installed or service is not available!!");
            if (YouTubeIntents.canResolvePlayVideoIntent(getContext())) {
                startActivity(YouTubeIntents.createPlayVideoIntent(getContext(), videoKey));
                return;
            }
            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + videoKey));
            startActivity(viewIntent);
            return;
        }
        if (view.getChildCount() == 0) {
            if (youTubePlayerFragment == null) {
                youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            }
            if (youTubePlayerFragment.isAdded()) {
                Log.e("Youtube Video", "Player Fragment Added");
                if (YTPlayerFragmentManager.youTubePlayer != null) {
                    Log.e("Youtube Video", "Player already Initialized so resetting");
                    pauseYoutubePlayer(YTPlayerFragmentManager.youTubePlayer);
                    releaseYoutubePlayer(YTPlayerFragmentManager.youTubePlayer);
                    YTPlayerFragmentManager.youTubePlayer = null;
                }

                getChildFragmentManager()
                        .beginTransaction()
                        .remove(youTubePlayerFragment)
                        .commit();

                getFragmentManager().executePendingTransactions();
                youTubePlayerFragment = null;
            }
            if (youTubePlayerFragment == null) {
                Log.e("Youtube Video", "Player Fragment Created");
                youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            }
            getFragmentManager()
                    .beginTransaction()
                    .add(view.getId(), youTubePlayerFragment)
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .commit();
            Log.e("Youtube Video", "Player Initialization Started");
            youTubePlayerFragment.initialize(
                    getYoutubeDeveloperKey(),
                    new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                            Log.e("Youtube Video", "Player Initialized");
                            YTPlayerFragmentManager.youTubePlayer = youTubePlayer;
                            if (!wasRestored) {
                                YTPlayerFragmentManager.youTubePlayer.loadVideo(videoKey, 0);
                            }

                            YTPlayerFragmentManager.youTubePlayer.setFullscreenControlFlags(0);
                            YTPlayerFragmentManager.youTubePlayer.setOnFullscreenListener(isFullScreen -> setFullScreen(isFullScreen));
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                            Log.e("Youtube Video", "Youtube Video Initialization failed " + youTubeInitializationResult.name());
                            if (YouTubeIntents.canResolvePlayVideoIntent(getContext())) {
                                startActivity(YouTubeIntents.createPlayVideoIntent(getContext(), videoKey));
                                return;
                            }
                            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + videoKey));
                            startActivity(viewIntent);
                        }
                    });
        } else {
            Log.e("Youtube Video", "Has already Fragment");
        }
    }

    private void pauseYoutubePlayer(YouTubePlayer player) {
        if (player != null)
            try {
                player.pause();
                player.release();
            } catch (Exception e) {
                Log.e("Youtube Video", "Player pause/release error" + e.getMessage());
            }
    }

    public void setFullScreen(boolean fullScreen) {
        this.isFullScreen = fullScreen;
    }

    private void releaseYoutubePlayer(YouTubePlayer player) {
        if (player != null) {
            try {
                player.release();
            } catch (Exception ignore) {
                Log.e("Youtube Video", "Player pause/release :: release error" + ignore.getMessage());
            }
        }
    }

    public void unBindPlayer(FrameLayout videoView) {
        if (videoView.getChildCount() > 0) {
            Log.e("Youtube Video", "Cleared!!");
            clearVideoPlayer();
        }
    }

    protected void clearVideoPlayer() {
        if (youTubePlayerFragment != null && youTubePlayerFragment.isAdded()) {
            if (YTPlayerFragmentManager.youTubePlayer != null) {
                pauseYoutubePlayer(YTPlayerFragmentManager.youTubePlayer);
                releaseYoutubePlayer(YTPlayerFragmentManager.youTubePlayer);
                YTPlayerFragmentManager.youTubePlayer = null;
            }

            getFragmentManager().beginTransaction().remove(youTubePlayerFragment).commit();
            try {
                getFragmentManager().executePendingTransactions();
            } catch (Exception ignore) {
                Log.e("Youtube Video", "Clear error: " + ignore.getMessage());
            }
            youTubePlayerFragment = null;
        }
    }

    public abstract String getYoutubeDeveloperKey();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        clearVideoPlayer();
        super.onDestroyView();
    }

    public boolean closeYoutubePlayer() {
        Log.e("Youtube Video", "Changing player to min-size screen "+ isFullScreen);
        if (isFullScreen && youTubePlayer != null){
            youTubePlayer.setFullscreen(false);
            this.isFullScreen = false;
            Log.e("Youtube Video", "player changed to min-size screen "+ isFullScreen);
            return true;
        }
        return false;
    }
}
