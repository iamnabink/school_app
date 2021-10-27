package com.swipecrafts.school.ui.dashboard.chat;


import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.viewmodel.VideoAlbumViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class ChatUserFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private VideoAlbumViewModel chatUserViewModel;

    private RecyclerView mChatUserRecyclerView;
    private ChatUserListAdapter mChatUserAdapter;
    private List<Teacher> chatUserList;

    private Toolbar toolbar;
    private SwipeRefreshLayout videoAlbumSwipeLyt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        //Set up and subscribe (observe) to the ViewModel
//        videoAlbumViewModel = ViewModelProviders.of(this, viewModelFactory).get(VideoAlbumViewModel.class);
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

        mChatUserRecyclerView = (RecyclerView) view.findViewById(R.id.videoAlbumRecyclerView);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Video Album");
        }

        // Setup refresh listener which triggers new data loading
        videoAlbumSwipeLyt.setOnRefreshListener(this::refreshUsersInChat);
        // Configure the refreshing colors
        videoAlbumSwipeLyt.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        // Set the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mChatUserRecyclerView.getContext(), layoutManager.getOrientation());
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.color.colorGrey));
        mChatUserRecyclerView.addItemDecoration(itemDecoration);
        mChatUserRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mChatUserRecyclerView.setLayoutManager(layoutManager);

        if (chatUserList == null) chatUserList = new ArrayList<>();
        mChatUserAdapter = new ChatUserListAdapter(chatUserList,
                (imageView, model) -> {
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.progressbar)
                            .error(R.drawable.placeholder);

                    Glide.with(this)
                            .load(model.getTeacherProfile())
                            .apply(options)
                            .into(imageView);
                },
                model -> {

                });
        mChatUserRecyclerView.setAdapter(mChatUserAdapter);
    }

    private void refreshUsersInChat() {
//        videoAlbumViewModel.refreshVideoAlbums(new ApiListener<List<Album>>() {
//            @Override
//            public void onSuccess(List<Album> response) {
//                if (response == null) return;
//                Log.e("VideoAlbumContent", response.size()+"");
//                setUpRecyclerView(response);
//                videoAlbumSwipeLyt.setRefreshing(false);
//            }
//
//            @Override
//            public void onFailed(String message) {
//
//            }
//        });
    }

    private void setUpRecyclerView(List<Teacher> chatUserList) {
        if (chatUserList == null) return;
        this.chatUserList = chatUserList;

        Log.e("VideoAlbumContent", chatUserList.size()+" ");

        mChatUserAdapter.updateVideoAlbums(chatUserList);
    }
}
