package com.swipecrafts.school.ui.dc.dcfeed;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.library.views.AudioControllerView;
import com.swipecrafts.library.views.ErrorView;
import com.swipecrafts.library.youtube.YTPlayerFragmentManager;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.utils.player.audio.AudioSource;
import com.swipecrafts.school.viewmodel.ContentFeedViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.swipecrafts.school.utils.player.MediaPlayerState.STATE_PLAYING;

public class ContentFeedFragment extends YTPlayerFragmentManager {
    public static final int AUDIO_PERMISSION_REQUEST_CODE = 102;
    public static final String[] WRITE_EXTERNAL_STORAGE_PERMS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS};

    private static final String ARG_GRADE_ID = "ARG_GRADE_ID";
    private static final String ARG_SUBJECT_ID = "ARG_SUBJECT_ID";
    private static final String ARG_CHAPTER_ID = "ARG_CHAPTER_ID";
    private static final String ARG_CHAPTER_NAME = "ARG_CHAPTER_NAME";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private long gradeId;
    private long subjectId;
    private long chapterId;
    private String chapterName = "";


    private RecyclerView mContentRecyclerView;
    private ContentFeedListAdapter mContentAdapter;
    private ContentFeedViewModel viewModel;
    private DCFeedAdapter mAdapter;


    private Toolbar toolbar;
    private SwipeRefreshLayout swipeContainer;
    private ErrorView errorLayout;

    private AudioSource source = null;
    private AudioControllerView audioControllerView;
    private ImageButton sourceButton;

    public static ContentFeedFragment newInstance(long gradeId, long subjectId, long chapterId, String chapterName) {
        ContentFeedFragment fragment = new ContentFeedFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_GRADE_ID, gradeId);
        args.putLong(ARG_SUBJECT_ID, subjectId);
        args.putLong(ARG_CHAPTER_ID, chapterId);
        args.putString(ARG_CHAPTER_NAME, chapterName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            gradeId = getArguments().getLong(ARG_GRADE_ID);
            subjectId = getArguments().getLong(ARG_SUBJECT_ID);
            chapterId = getArguments().getLong(ARG_CHAPTER_ID);
            chapterName = getArguments().getString(ARG_CHAPTER_NAME);
        }

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ContentFeedViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_feed_list, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.dcContentToolbar);
        toolbar.setTitle(chapterName + " Feed");

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.contentSwipeToRefreshLayout);

        mContentRecyclerView = (RecyclerView) view.findViewById(R.id.contentFeedRecyclerView);

        errorLayout = (ErrorView) view.findViewById(R.id.errorLayout);
        audioControllerView = (AudioControllerView) view.findViewById(R.id.audioPlayerView);
    }

    private void init() {

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this::refreshContentFeed);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mContentRecyclerView.setLayoutManager(layoutManager);

        audioControllerView.setMediaListener((state) -> {
            if (state == STATE_PLAYING) {
                if (sourceButton != null) sourceButton.setImageResource(R.drawable.ic_pause);
            } else {
                if (sourceButton != null) sourceButton.setImageResource(R.drawable.ic_play);
            }
        });
        mAdapter = new DCFeedAdapter(
                this,
                new ArrayList<>(),
                model -> {

                },
                (model, playBtn) -> {
                    audioControllerView.initializePlayer();
                    initialize();
                    boolean isSourceSame = source != null && (source.getIdentifier() == model.getDcContentId());
                    if (isSourceSame && audioControllerView.isPlaying()) {
                        audioControllerView.pauseAudio();
                        playBtn.setImageResource(R.drawable.ic_play);
                        audioControllerView.closeWithAnimation();
                    } else {
                        this.source = new AudioSource(model.getDcContentId(), model.getDcContentTitle(), model.getDcContentLinkKey());

                        if (sourceButton != null) sourceButton.setImageResource(R.drawable.ic_play);
                        audioControllerView.loadAudioSource(source);
                        audioControllerView.openWithAnimation();
                        playBtn.setImageResource(R.drawable.ic_pause);
                        this.sourceButton = playBtn;
                    }
                });

        mContentRecyclerView.setAdapter(mAdapter);

        swipeContainer.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        errorLayout.setViewType(ErrorView.PROGRESS).apply();
        refreshContentFeed();
    }

    private void refreshContentFeed() {
        viewModel.loadDCContentFeed(gradeId, subjectId, chapterId).observe(this, response -> {
            if (response == null || response.isLoading()) return;

            if (response.isSuccessful()) {
                swipeContainer.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
                clearVideoPlayer();
                mAdapter.setDcFeeds(response.data);
            } else {
                String message = Utils.isOnline(getContext()) ?
                        (response.status == 1001 ? Constants.UNKNOWN_ERROR_MESSAGE : response.message)
                        : Constants.NO_INTERNET_MESSAGE;
                if (mAdapter.getItemCount() != 0) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    return;
                }
                swipeContainer.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                errorLayout
                        .setViewType(ErrorView.ERROR_WITH_BUTTON)
                        .setErrorTitle(message)
                        .setButtonText(getResources().getString(R.string.dialog_retry))
                        .setOnRetryListener(v -> {
                            errorLayout.setViewType(ErrorView.PROGRESS).apply();
                            refreshContentFeed();
                        })
                        .apply();

                swipeContainer.setRefreshing(false);
            }
        });

    }

    private void initialize() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(WRITE_EXTERNAL_STORAGE_PERMS, AUDIO_PERMISSION_REQUEST_CODE);
        } else {
            audioControllerView.initVisualiser();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case AUDIO_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    audioControllerView.initVisualiser();
                }
        }
    }

    @Override
    public void onDestroy() {
        if (audioControllerView != null) {
            audioControllerView.release();
        }
        super.onDestroy();
    }

    @Override
    public String getYoutubeDeveloperKey() {
        return getResources().getString(R.string.YOUTUBE_DEVELOPER_KEY);
    }
}