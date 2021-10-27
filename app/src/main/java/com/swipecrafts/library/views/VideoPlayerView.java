package com.swipecrafts.library.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.youtube.player.YouTubeThumbnailView;

/**
 * Created by Madhusudan Sapkota on 7/2/2018.
 */
//unused
public class VideoPlayerView extends ViewGroup {

    private int playerId;

    public VideoPlayerView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public VideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        YouTubeThumbnailView youTubeThumbnailView = new YouTubeThumbnailView(getContext());
        ImageButton playButton = new ImageButton(getContext());


        addView(frameLayout, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(youTubeThumbnailView, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(playButton, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setVideoPlayerId(int id) {
        this.playerId = id;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) { }
}
