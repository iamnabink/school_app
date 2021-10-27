package com.swipecrafts.school.ui.dc.Video;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.swipecrafts.library.views.AudioControllerView;
import com.swipecrafts.library.views.ErrorView;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.utils.player.audio.AudioSource;
import com.swipecrafts.school.viewmodel.VideoViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class VideoFragment extends BaseFragment {

    private static final String ARG_GRADE_ID = "ARG_GRADE_ID";
    private static final String ARG_SUBJECT_ID = "ARG_SUBJECT_ID";
    private static final String ARG_SUBJECT_NAME = "ARG_SUBJECT_NAME";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private long gradeId;
    private long subjectID;
    private String subjectName = "";

    private RecyclerView recyclerView;
    private VideoViewAdapter mAdapter;
    private List<VideoFeed> videoList;
    Boolean isScrolling = false;
    int currentItems,totalItems,scrollOutItems;
    Boolean isLoading = false;

    private VideoViewModel viewModel;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeContainer;
    private ErrorView errorLayout;

    private AudioSource source = null;
    private AudioControllerView audioControllerView;
    private ImageButton sourceButton;

    @RequiresApi(api = Build.VERSION_CODES.M)

    public VideoFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static VideoFragment newInstance(long gradeId, long subjectID, String subjectName) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_GRADE_ID, gradeId);
        args.putLong(ARG_SUBJECT_ID, subjectID);
        args.putString(ARG_SUBJECT_NAME, subjectName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            gradeId = getArguments().getLong(ARG_GRADE_ID);
            subjectID = getArguments().getLong(ARG_SUBJECT_ID);
            subjectName = getArguments().getString(ARG_SUBJECT_NAME);
        }

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VideoViewModel.class);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_video, container,false);
        findView(view);
        init();
//        fetchData();
        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.dcVideoToolbar);
        toolbar.setTitle(subjectName + " Video");

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.videoSwipeToRefreshLayout);
        errorLayout = (ErrorView) view.findViewById(R.id.errorLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.videoRecyclerView);
        audioControllerView = (AudioControllerView) view.findViewById(R.id.audioPlayerView);
    }

    private void init() {

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this::refreshVideo);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);

        if (videoList == null) videoList = new ArrayList<>();
        mAdapter = new VideoViewAdapter(
                videoList,getContext());

        recyclerView.setAdapter(mAdapter);



//        // set RecyclerView on item click listner
//        mAdapter.setOnItemListener(new VideoViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(VideoFeed item) {
//                String content = "";
//                String title = "";
//                String page_number = "";
//                try{
//                    content = item.getDc_video_content();
//                    title = item.getIs_video_title();
//                    page_number = item.getDc_video_page();
//                }catch (Exception e){
//                    System.out.print(e.getMessage());
//                }
//            }
//        });
//
//        //set load more listener for the RecyclerView adapter
//        mAdapter.setOnLoadMoreListener(new VideoViewAdapter.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                if (videoList.size() <= 100) {
//                    videoList.add(null);
//                    mAdapter.notifyItemInserted(videoList.size() - 1);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            videoList.remove(videoList.size() - 1);
//                            mAdapter.notifyItemRemoved(videoList.size());
//
//                            //Generating more data
//                            int index = videoList.size();
//                            int end = index + 2;
//                            for (int i = index; i < end; i++) {
//                                VideoFeed videoFeed = new VideoFeed();
//                                videoFeed.getDc_video_content();
//                                videoFeed.getDc_video_page();
//                                videoFeed.getIs_video_title();
//                                videoList.add(videoFeed);
//                            }
//                        }
//                    }, 5000);
//                } else {
//                    Toast.makeText(getContext(), "Loading data completed", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });


//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (!recyclerView.canScrollHorizontally(1) && isMoreDataAvailable && !isLoading) {
//                    isLoading = true;
//                    pb_loading.setVisibility(View.VISIBLE);
//                    PrepareData(ppage,pcount);
//                }
//            }
//        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL);
//            if (!recyclerView.canScrollHorizontally(1) && !isScrolling)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems));
                {
                    isScrolling = false;
//                    fetchData();
                }

            }
        });

        swipeContainer.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        errorLayout.setViewType(ErrorView.PROGRESS).apply();
        refreshVideo();
    }
//
//    private void fetchData() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                for (int i=0;i<2;i++)
//                {
//                   VideoFeed item = new VideoFeed();
//                item.getIs_video_title();
//                item.getDc_video_page();
//                item.getDc_video_content();
//
//                    mAdapter.notifyDataSetChanged();
//
//                }
//            }
//        },5000);
//    }

    private void setUpRecyclerView(List<VideoFeed> videos){
        if (videos == null) return;
        this.videoList = videos;

        if (mAdapter != null) mAdapter.setVideoList(videos);
    }

    private void refreshVideo() {
        viewModel.loadDCVideoFeed(gradeId, subjectID).observe(this, response -> {
            if (response == null || response.isLoading()) return;

            if (response.isSuccessful()) {
                swipeContainer.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);

                mAdapter.setVideoList(response.data);
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
                            refreshVideo();
                        })
                        .apply();

                swipeContainer.setRefreshing(false);
            }
        });

    }


}
