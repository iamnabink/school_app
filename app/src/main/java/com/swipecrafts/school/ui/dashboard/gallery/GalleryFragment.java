package com.swipecrafts.school.ui.dashboard.gallery;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.db.ImageAlbumContent;
import com.swipecrafts.school.ui.dashboard.gallery.adapters.GalleryAdapter;
import com.swipecrafts.school.utils.DisplayUtility;
import com.swipecrafts.school.viewmodel.GalleryViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GalleryFragment extends Fragment {

    private static String ALBUM_ID_KEY = "AlbumId";
    private static String ALBUM_NAME_KEY = "AlbumName";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private Toolbar toolbar;
    private RecyclerView mAlbumRecyclerView;
    private GalleryViewModel albumViewModel;

    private List<ImageAlbumContent> imageAlbumContents;
    private GalleryAdapter imageGalleryAdapter;

    private long albumId = 0;
    private String albumName = "Album Details";
    private SwipeRefreshLayout swipeContainer;

    public static GalleryFragment getInstance(long albumId, String albumName) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ALBUM_ID_KEY, albumId);
        bundle.putString(ALBUM_NAME_KEY, albumName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            albumId = getArguments().getLong(ALBUM_ID_KEY);
            albumName = getArguments().getString(ALBUM_NAME_KEY);
        }
        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        albumViewModel = ViewModelProviders.of(this, viewModelFactory).get(GalleryViewModel.class);
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

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.imageGallerySwipeToRefreshLayout);

        mAlbumRecyclerView = view.findViewById(R.id.albumRecyclerView);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(albumName);
        }

        swipeContainer.setEnabled(false);

        albumViewModel.getAlbumContents(albumId).observe(this, albumContents -> {
            if (albumContents == null) return;
            this.imageAlbumContents = albumContents;
            setUpRecyclerView(albumContents);
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (imageAlbumContents == null) imageAlbumContents = new ArrayList<>();
        initRecyclerView(imageAlbumContents);
    }

    private void initRecyclerView(List<ImageAlbumContent> albums) {
        int numOfColumns;
        if (DisplayUtility.isInLandscapeMode(getContext())) numOfColumns = 4;
        else numOfColumns = 3;

        mAlbumRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));
        imageGalleryAdapter = new GalleryAdapter(getContext(), albums,

                (imageView, model) -> {
                    RequestOptions options = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .placeholder(R.drawable.progressbar)
                            .error(R.drawable.placeholder);

                    Glide.with(this)
                            .load(model.getC1Url())
                            .apply(options)
                            .into(imageView);
                },
                (position) -> {
                    Intent intent = new Intent(getActivity(), FullScreenImageGalleryActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(FullScreenImageGalleryActivity.KEY_IMAGES, extractUrls());
                    bundle.putInt(FullScreenImageGalleryActivity.KEY_POSITION, position);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
        );
        mAlbumRecyclerView.setAdapter(imageGalleryAdapter);
    }


    private void setUpRecyclerView(List<ImageAlbumContent> imageAlbumContents) {
        if (imageGalleryAdapter != null)
            imageGalleryAdapter.updateAlbums(imageAlbumContents);
        else
            initRecyclerView(imageAlbumContents);
    }

    private ArrayList<String> extractUrls() {
        ArrayList<String> urls = new ArrayList<>();

        for (ImageAlbumContent content : imageAlbumContents) {
            urls.add(content.getC1Url());
        }
        return urls;
    }
}
