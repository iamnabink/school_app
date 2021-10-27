package com.swipecrafts.school.ui.dashboard.gallery;


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
import com.swipecrafts.school.data.model.db.Album;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dashboard.gallery.adapters.AlbumAdapter;
import com.swipecrafts.school.utils.DisplayUtility;
import com.swipecrafts.school.viewmodel.GalleryViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class AlbumFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private Toolbar toolbar;
    private RecyclerView mAlbumRecyclerView;
    private GalleryViewModel albumViewModel;

    private List<Album> albumList;
    private AlbumAdapter imageGalleryAdapter;

    private SwipeRefreshLayout swipeContainer;
    private LinearLayout errorLayout;
    private boolean isFirstRefresh = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        albumViewModel = ViewModelProviders.of(this, viewModelFactory).get(GalleryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.albumToolbar);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.imageAlbumSwipeToRefreshLayout);
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);

        mAlbumRecyclerView = view.findViewById(R.id.albumRecyclerView);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this::refreshAlbums);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        albumViewModel.countAlbums().observe(this, count -> {
            if (count == null) return;

            if (count <= 0) {
                isFirstRefresh = true;
                swipeContainer.post(() -> swipeContainer.setRefreshing(true));
                refreshAlbums();
            }
        });

        if (albumList == null) albumList = new ArrayList<>();
        initRecyclerView(albumList);
        albumViewModel.getAlbums().observe(this, albums -> {
            if (albums == null) return;

            if (imageGalleryAdapter != null)
                imageGalleryAdapter.updateAlbums(albums);
            else
                initRecyclerView(albums);
        });
    }

    private void refreshAlbums() {
        albumViewModel.refreshAlbums().observe(this, (result -> {
            if (result == null) return;

            if (result.second) {
                mAlbumRecyclerView.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
            } else {
                swipeContainer.setRefreshing(false);
                if (!isFirstRefresh){
                    Toast.makeText(getContext(), result.first, Toast.LENGTH_SHORT).show();
                    return;
                }
                mAlbumRecyclerView.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                TextView msg = errorLayout.findViewById(R.id.error_message);
                msg.setText(result.first);
            }
        }));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (albumList == null) albumList = new ArrayList<>();
        initRecyclerView(albumList);
    }

    private void initRecyclerView(List<Album> albums) {
        int numOfColumns;
        if (DisplayUtility.isInLandscapeMode(getContext())) numOfColumns = 3;
        else numOfColumns = 2;

        mAlbumRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));
        imageGalleryAdapter = new AlbumAdapter(getContext(), albums,
                (imageView, model) -> {
                    String imgUrl = model.getAlbumImgUrl();
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
                    GalleryFragment fragment = GalleryFragment.getInstance(model.getAlbumId(), model.getAlbumName());
                    replaceFragment(R.id.main_container, fragment, GalleryFragment.class.getSimpleName(), "ChatUserFragment");
                }
        );
        mAlbumRecyclerView.setAdapter(imageGalleryAdapter);
    }
}
