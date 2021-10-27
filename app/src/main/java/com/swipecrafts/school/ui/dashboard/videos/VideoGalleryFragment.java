package com.swipecrafts.school.ui.dashboard.videos;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.utils.DisplayUtility;
import com.swipecrafts.library.youtube.YoutubeVideoPlayerActivity;
import com.swipecrafts.school.viewmodel.VideoAlbumViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class VideoGalleryFragment extends Fragment {

    private static String ALBUM_ID_KEY = "AlbumId";
    private static String ALBUM_NAME_KEY = "AlbumName";
    private static String ALBUM_ARRAY_DATA = "AlbumList";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private Toolbar toolbar;
    private RecyclerView mAlbumRecyclerView;
    private VideoAlbumViewModel videoGalleryViewModel;

    private List<VideoAlbumContent> videoAlbumContents;
    private VideoGalleryAdapter videoGalleryAdapter;

    private long albumId = 0;
    private String albumName = "Album Details";

    private VideoAlbum album;

//    public static VideoGalleryFragment getInstance(long albumId, String albumName) {
//        VideoGalleryFragment fragment = new VideoGalleryFragment();
//        Bundle bundle = new Bundle();da
//        bundle.putLong(ALBUM_ID_KEY, albumId);
//        bundle.putString(ALBUM_NAME_KEY, albumName);
//        fragment.setArguments(bundle);
//        return fragment;
//    }

    public static VideoGalleryFragment getInstance(VideoAlbum album) {
        VideoGalleryFragment fragment = new VideoGalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ALBUM_ARRAY_DATA, album);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
//            albumId = getArguments().getLong(ALBUM_ID_KEY);
//            albumName = getArguments().getString(ALBUM_NAME_KEY);
            album = (VideoAlbum) getArguments().getSerializable(ALBUM_ARRAY_DATA);
            this.videoAlbumContents = album.getAlbumContent();
        }
        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        videoGalleryViewModel = ViewModelProviders.of(this, viewModelFactory).get(VideoAlbumViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.galleryToolbar);

        mAlbumRecyclerView = view.findViewById(R.id.albumRecyclerView);

    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(albumName);
        }

        setUpRecyclerView(videoAlbumContents);
//        videoGalleryViewModel.refreshVideoAlbums(albumId).observe(this, albumContents -> {
//            if (albumContents == null) return;
//            this.videoAlbumContents = albumContents;
//            setUpRecyclerView(albumContents);
//        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (videoAlbumContents == null) videoAlbumContents = new ArrayList<>();
        initRecyclerView(videoAlbumContents);
    }

    private void initRecyclerView(List<VideoAlbumContent> videoAlbumContents) {
        int numOfColumns;
        if (DisplayUtility.isInLandscapeMode(getContext())) numOfColumns = 4;
        else numOfColumns = 3;

        mAlbumRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));
        videoGalleryAdapter = new VideoGalleryAdapter(
                getContext(),
                videoAlbumContents,
                (imageView, model) -> {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.progressbar)
                            .error(R.drawable.placeholder);

                    Glide.with(this)
                            .load(model.getCthumbnail())
                            .apply(options)
                            .into(imageView);
                },
                (model) -> {
                    // go to the video player
                    Intent intent = new Intent(getActivity(), YoutubeVideoPlayerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(YoutubeVideoPlayerActivity.ARG_VIDEO_KEY, model.getClinkKey());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
        );
        mAlbumRecyclerView.setAdapter(videoGalleryAdapter);
    }


    private void setUpRecyclerView(List<VideoAlbumContent> imageAlbumContents) {
        if (videoGalleryAdapter != null)
            videoGalleryAdapter.updateAlbums(imageAlbumContents);
        else
            initRecyclerView(imageAlbumContents);
    }
}
