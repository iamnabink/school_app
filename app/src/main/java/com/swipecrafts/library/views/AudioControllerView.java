package com.swipecrafts.library.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresPermission;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.swipecrafts.library.visualizer.VisualizerManager;
import com.swipecrafts.library.visualizer.renderer.ColumnarDynamicBarRenderer;
import com.swipecrafts.library.visualizer.renderer.IRenderer;
import com.swipecrafts.school.R;
import com.swipecrafts.school.utils.DisplayUtility;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.utils.ViewHelper;
import com.swipecrafts.school.utils.player.MediaPlayer;
import com.swipecrafts.school.utils.player.MediaPlayerState;
import com.swipecrafts.school.utils.player.audio.AudioPlayer;
import com.swipecrafts.school.utils.player.audio.AudioSource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Madhusudan Sapkota on 7/3/2018.
 */
public class AudioControllerView extends RelativeLayout {

    private int backgroundColor;
    private int shadowColor;
    private @ShadowGravity
    int shadowDirection;
    private float radiusCorner;
    private float elevation;

    private Animation slideInAnimation;
    private Animation slideOutAnimation;

    private AudioPlayer audioPlayer;

    private SeekBar audioSeekBar;
    private TextView audioTimeTV;
    private TextView audioTitleTV;
    private SurfaceView audioVisualiser;
    private ImageButton playButton;
    private ImageButton closeButton;

    private boolean isPlayerInitialized = false;
    private boolean isAudioPlaying = false;
    private AudioSource audioSource;
    private MediaPlayer.MediaPlayerListener mediaListener;
    private VisualizerManager visualizerManager;
    private boolean isVisualizerInitialized = false;

    public AudioControllerView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public AudioControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public AudioControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        initTypeValues(context, attrs, defStyleAttr);

        setBackground(ViewHelper.generateBackgroundWithShadow(
                this,
                backgroundColor,
                shadowColor, shadowDirection,
                radiusCorner, elevation
        ));

        initComponent(context);

        playButton.setOnClickListener(v -> {
            if (!isPlayerInitialized) return;
            if (isAudioPlaying) {
                pauseAudio();
            } else {
                playAudio();
            }
        });

        closeButton.setOnClickListener(v -> {
            if (audioPlayer != null && isAudioPlaying) audioPlayer.stopPlayback();
            if (mediaListener != null) mediaListener.onStateChanged(MediaPlayerState.STATE_PAUSED);
            closeWithAnimation();
        });
    }

    private void initTypeValues(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AudioControllerView, defStyleAttr, 0);
        try {
            setBackgroundColor(
                    a.getColor(R.styleable.AudioControllerView_backgroundColor, Color.WHITE)
            );

            setShadowColor(
                    a.getColor(R.styleable.AudioControllerView_shadowColor, Color.LTGRAY)
            );

            setRadiusCorner(
                    a.getDimensionPixelSize(R.styleable.AudioControllerView_radiusCorner, 0)
            );

            setViewElevation(
                    a.getDimensionPixelSize(R.styleable.AudioControllerView_elevation, 0)
            );

            int shadowGravityIndex = a.getInteger(R.styleable.AudioControllerView_shadowDirection, 1);
            setShadowDirection(shadowGravityIndex == 0 ? Gravity.TOP : shadowGravityIndex == 1 ? Gravity.BOTTOM : Gravity.CENTER);

        } finally {
            a.recycle();
        }
    }

    private void initComponent(Context context) {
        int px_of_2dp = DisplayUtility.dipToPixels(context, 2);
        int px_of_5dp = DisplayUtility.dipToPixels(context, 5);

        slideInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.side_in);
        slideOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.side_out);

        RelativeLayout root = new RelativeLayout(context);
        root.setPadding(px_of_5dp, px_of_5dp, px_of_5dp, px_of_5dp);
        root.setPaddingRelative(px_of_5dp, px_of_5dp, px_of_5dp, px_of_5dp);
        root.setBackgroundColor(Color.TRANSPARENT);

        ViewGroup.LayoutParams root_LayoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        root_LayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        root_LayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        root.setLayoutParams(root_LayoutParams);
        addView(root);

        audioSeekBar = new SeekBar(context);
        audioSeekBar.setId(R.id.audio_player_seekBar);
        audioSeekBar.setMax(100);
        audioSeekBar.setProgress(0);
        audioSeekBar.setSecondaryProgress(0);
        audioSeekBar.setOnTouchListener((v, event) -> true);
        audioSeekBar.setPaddingRelative(px_of_2dp, px_of_2dp, px_of_2dp, px_of_2dp);

        audioTimeTV = new TextView(context);
        audioTimeTV.setId(R.id.audio_player_time);
        audioTimeTV.setPaddingRelative(px_of_2dp, px_of_2dp, px_of_2dp, px_of_2dp);
        audioTimeTV.setText("00:00");
        audioTimeTV.setTextColor(Color.BLACK);
        audioTimeTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        audioTitleTV = new TextView(context);
        audioTitleTV.setId(R.id.audio_player_title);
        audioTitleTV.setPaddingRelative(px_of_5dp, 0, px_of_5dp, 0);
        audioTitleTV.setText(R.string.now_playing);
        audioTitleTV.setTextAlignment(TEXT_ALIGNMENT_GRAVITY);
        audioTitleTV.setGravity(Gravity.CENTER_VERTICAL);
        audioTitleTV.setTextColor(Color.BLACK);
        audioTitleTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        audioVisualiser = new SurfaceView(context);
        audioVisualiser.setId(R.id.audio_player_visualiser);
        audioVisualiser.setBackgroundColor(Color.TRANSPARENT);
        audioVisualiser.setZOrderOnTop(true);
        audioVisualiser.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        playButton = new ImageButton(getContext(), null, android.R.style.MediaButton);
        playButton.setId(R.id.audio_player_play_btn);
        playButton.setImageResource(R.drawable.ic_play);
        playButton.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);


        closeButton = new ImageButton(getContext(), null, android.R.style.MediaButton);
        closeButton.setId(R.id.audio_player_close_btn);
        closeButton.setImageResource(R.drawable.ic_close);
        closeButton.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);


        RelativeLayout.LayoutParams audioTime_LayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        audioTime_LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        audioTime_LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        audioTime_LayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        audioTimeTV.setLayoutParams(audioTime_LayoutParams);
        root.addView(audioTimeTV);

        RelativeLayout.LayoutParams audioProgress_LayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        audioProgress_LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        audioProgress_LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        audioProgress_LayoutParams.addRule(RelativeLayout.START_OF, audioTimeTV.getId());
        audioSeekBar.setLayoutParams(audioProgress_LayoutParams);
        root.addView(audioSeekBar);

        RelativeLayout.LayoutParams now_playing_LayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        now_playing_LayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        now_playing_LayoutParams.topMargin = px_of_2dp;
        now_playing_LayoutParams.setMargins(px_of_2dp, px_of_2dp, px_of_2dp, px_of_2dp);
        now_playing_LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        now_playing_LayoutParams.addRule(RelativeLayout.BELOW, audioSeekBar.getId());
        now_playing_LayoutParams.addRule(RelativeLayout.START_OF, playButton.getId());
        audioTitleTV.setLayoutParams(now_playing_LayoutParams);
        root.addView(audioTitleTV);

        RelativeLayout.LayoutParams audioVisualiser_LayoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, DisplayUtility.dipToPixels(context, 30));
        audioVisualiser_LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        audioVisualiser_LayoutParams.addRule(RelativeLayout.BELOW, audioTitleTV.getId());
        audioVisualiser_LayoutParams.addRule(RelativeLayout.START_OF, playButton.getId());
        audioVisualiser.setLayoutParams(audioVisualiser_LayoutParams);
        root.addView(audioVisualiser);

        RelativeLayout.LayoutParams btnPlay_LayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        btnPlay_LayoutParams.addRule(RelativeLayout.BELOW, audioTimeTV.getId());
        btnPlay_LayoutParams.setMargins(px_of_2dp, px_of_2dp, px_of_2dp, px_of_2dp);
        btnPlay_LayoutParams.addRule(RelativeLayout.START_OF, this.closeButton.getId());
        playButton.setLayoutParams(btnPlay_LayoutParams);
        root.addView(playButton);

        RelativeLayout.LayoutParams btnClose_LayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        btnClose_LayoutParams.setMargins(px_of_2dp, px_of_2dp, px_of_2dp, px_of_2dp);
        btnClose_LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        btnClose_LayoutParams.addRule(RelativeLayout.BELOW, audioTimeTV.getId());
        closeButton.setLayoutParams(btnClose_LayoutParams);
        root.addView(closeButton);
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setShadowColor(int color) {
        this.shadowColor = color;
    }

    public void setRadiusCorner(float radiusCorner) {
        this.radiusCorner = radiusCorner;
    }

    public void setViewElevation(int elevation) {
        this.elevation = elevation;
    }

    public void setShadowDirection(@ShadowGravity int shadowDirection) {
        this.shadowDirection = shadowDirection;
    }

    public void setMediaListener(MediaPlayer.MediaPlayerListener listener) {
        this.mediaListener = listener;
    }

    public void loadAudioSource(AudioSource source) {
        this.audioSource = source;
        if (!isPlayerInitialized)
            throw new UnsupportedOperationException("You must call initializePlayer() first!");
        String title = audioSource != null ? audioSource.getAudioTitle() : "Now Playing";
        audioTitleTV.setText(title);
        this.audioPlayer.loadMediaSource(source);
        this.audioPlayer.startPlayback(false);
        this.playAudio();
    }

    public void pauseAudio() {
        audioPlayer.pausePlayback();
        playButton.setImageResource(R.drawable.ic_play);
    }

    public void playAudio() {
        audioPlayer.resumePlayback();
        playButton.setImageResource(R.drawable.ic_pause);
        audioVisualiser.setVisibility(VISIBLE);
    }

    public void closeWithAnimation() {
        if (getVisibility() == INVISIBLE || getVisibility() == GONE) return;
        this.audioVisualiser.setVisibility(INVISIBLE);
        this.startAnimation(slideOutAnimation);
        this.setVisibility(View.GONE);
        audioTitleTV.setText("Now Playing");
    }

    public void openWithAnimation() {
        if (getVisibility() == VISIBLE) return;
        String title = audioSource != null ? audioSource.getAudioTitle() : "Now Playing";
        audioTitleTV.setText(title);
        this.audioVisualiser.setVisibility(INVISIBLE);
        this.startAnimation(slideInAnimation);
        this.setVisibility(View.VISIBLE);
    }

    @SuppressLint("MissingPermission")
    public void initializePlayer() {
        if (!isPlayerInitialized) {
            audioPlayer = new AudioPlayer(getContext(),
                    (status) -> {
                        switch (status) {
                            case MediaPlayerState.STATE_IDLE:
                                long time = audioPlayer.getDuration() == C.TIME_UNSET ? 0 : audioPlayer.getDuration();
                                audioTimeTV.setText(Utils.formatTime(time / 1000));
                                break;
                            case MediaPlayerState.STATE_CONNECTING:
                                break;
                            case MediaPlayerState.STATE_PLAYING:
                                initVisualiser();
                                isAudioPlaying = true;
                                audioVisualiser.setVisibility(VISIBLE);
                                playButton.setImageResource(R.drawable.ic_pause);
                                break;
                            case MediaPlayerState.STATE_PAUSED:
                                stop();
                                playButton.setImageResource(R.drawable.ic_play);
                                audioSource.setProgress(audioPlayer.getCurrentPosition());
                                isAudioPlaying = false;
                                break;
                            case MediaPlayerState.STATE_ENDED:
                                stop();
                                playButton.setImageResource(R.drawable.ic_play);
                                audioSource.setProgress(0);
                                audioSeekBar.setProgress(0);
                                audioVisualiser.setVisibility(INVISIBLE);
                                isAudioPlaying = false;
                                audioSource = null;
                                break;
                        }

                        if (mediaListener != null) mediaListener.onStateChanged(status);
                    },
                    (progress, bufferedProgress, duration) -> {
                        long remaining = duration - progress;
                        if (remaining <= 0) return;
                        String time = Utils.formatTime(remaining / 1000);
                        audioTimeTV.post(() -> audioTimeTV.setText(time));
                        audioSeekBar.post(() -> audioSeekBar.setProgress((int) ((progress * 100) / duration)));
                    }
            );
            isPlayerInitialized = true;
        }
    }

    @RequiresPermission(android.Manifest.permission.RECORD_AUDIO)
    public void initVisualiser() {
        if (visualizerManager == null) {
            VisualizerManager nvm = new VisualizerManager();
            isVisualizerInitialized = nvm.init(audioPlayer.getSessionId());
            visualizerManager = nvm;
        }
        if (isVisualizerInitialized) {
            visualizerManager.start(audioVisualiser, new IRenderer[]{new ColumnarDynamicBarRenderer()});
        }
    }

    public void stop() {
        this.audioVisualiser.setVisibility(INVISIBLE);
        if (visualizerManager != null && isVisualizerInitialized) visualizerManager.stop();
    }

    public void release() {
        if (visualizerManager != null && isVisualizerInitialized) visualizerManager.release();
        if (audioPlayer != null) audioPlayer.tearDown();

        this.visualizerManager = null;
        this.audioPlayer = null;
    }

    public boolean isPlaying() {
        return isAudioPlaying;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Gravity.TOP, Gravity.BOTTOM, Gravity.CENTER})
    public @interface ShadowGravity {
    }
}
