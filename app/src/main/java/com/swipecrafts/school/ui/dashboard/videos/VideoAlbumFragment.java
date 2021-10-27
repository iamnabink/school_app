package com.swipecrafts.school.ui.dashboard.videos;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.utils.DisplayUtility;
import com.swipecrafts.school.viewmodel.VideoAlbumViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class VideoAlbumFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private VideoAlbumViewModel videoAlbumViewModel;

    private RecyclerView mAlbumRecyclerView;
    private VideoAlbumAdapter videoAlbumAdapter;
    private List<VideoAlbum> videoAlbumList;

    private Toolbar toolbar;
    private SwipeRefreshLayout videoAlbumSwipeLyt;
    private LinearLayout errorLayout;
    private boolean isFirstRefresh = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        //Set up and subscribe (observe) to the ViewModel
        videoAlbumViewModel = ViewModelProviders.of(this, viewModelFactory).get(VideoAlbumViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_album, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.videoAlbumToolbar);

        // Lookup the swipe container view
        videoAlbumSwipeLyt = (SwipeRefreshLayout) view.findViewById(R.id.videoAlbumSwipeRefreshLyt);
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);

        mAlbumRecyclerView = (RecyclerView) view.findViewById(R.id.videoAlbumRecyclerView);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Video Album");
        }

        // Setup refresh listener which triggers new data loading
        videoAlbumSwipeLyt.setOnRefreshListener(this::refreshVideoAlbum);
        // Configure the refreshing colors
        videoAlbumSwipeLyt.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        // Set the adapter
        if (videoAlbumList == null) videoAlbumList = new ArrayList<>();
        initRecyclerView(videoAlbumList);

        isFirstRefresh = true;
        videoAlbumSwipeLyt.post( () -> videoAlbumSwipeLyt.setRefreshing(true));
        refreshVideoAlbum();
    }

    private void refreshVideoAlbum() {
        videoAlbumViewModel.refreshVideoAlbums().observe(this, data ->{
            if (data == null) return;

            if (data.status()){
                videoAlbumSwipeLyt.setRefreshing(false);
                setUpRecyclerView(data.getData());
                mAlbumRecyclerView.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
            }else  {
                videoAlbumSwipeLyt.setRefreshing(false);
                if (!isFirstRefresh){
                    Toast.makeText(getContext(), data.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                mAlbumRecyclerView.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                TextView msg = errorLayout.findViewById(R.id.error_message);
                msg.setText(data.getMessage());
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (videoAlbumList == null) videoAlbumList = new ArrayList<>();
        initRecyclerView(videoAlbumList);
    }

    private void initRecyclerView(List<VideoAlbum> albums) {
        int numOfColumns;
        if (DisplayUtility.isInLandscapeMode(getContext())) numOfColumns = 3;
        else numOfColumns = 2;

        mAlbumRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));
        videoAlbumAdapter = new VideoAlbumAdapter(getContext(), albums,
                (imageView, model) -> {
                    String imgUrl = model.getAlbumContent().get(0).getCthumbnail();
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.progressbar)
                            .error(R.drawable.placeholder);

                    Glide.with(this)
                            .load(imgUrl)
                            .apply(options)
                            .into(imageView);
                },
                (model) -> {
                    VideoGalleryFragment fragment = VideoGalleryFragment.getInstance(model);
                    replaceFragment(R.id.main_container, fragment, VideoGalleryFragment.class.getSimpleName(), "VideoAlbumFragment");
                }
        );
        mAlbumRecyclerView.setAdapter(videoAlbumAdapter);
    }

    private void setUpRecyclerView(List<VideoAlbum> videoAlbums) {
        if (videoAlbums == null) return;
        this.videoAlbumList = videoAlbums;

        Log.e("VideoAlbumContent", videoAlbums.size() + " ");

        videoAlbumAdapter.updateVideoAlbums(videoAlbums);
    }
}
