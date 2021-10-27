package com.swipecrafts.school.ui.dc.Video;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.swipecrafts.school.R;
import java.util.List;

import bg.devlabs.fullscreenvideoview.FullscreenVideoView;

//public class VideoViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//    private List<VideoFeed> videoList;
//    Context context;
//    private OnItemClickListener listener;
//
//    // for load more
//    private final int VIEW_TYPE_ITEM = 0;
//    private final int VIEW_TYPE_LOADING = 1;
//    private OnLoadMoreListener onLoadMoreListener;
//
//    // The minimum amount of items to have below your current scroll position
//    // before loading more.
//    private boolean isLoading;
//    private int visibleThreshold = 3;
//    private int lastVisibleItem, totalItemCount;
//
//    public void setVideoList(List<VideoFeed> videoList) {
//            if (videoList == null) return;
//            this.videoList.clear();
//
//            this.videoList = videoList;
//            notifyDataSetChanged();
//        }
//
//    public interface OnItemClickListener {
//        void onItemClick(VideoFeed item);
//    }
//
//    public interface OnLoadMoreListener {
//        void onLoadMore();
//    }
//
//    // Provide a suitable constructor (depends on the kind of dataset)
//    public VideoViewAdapter(List<VideoFeed> videoList, Context context, RecyclerView recyclerView) {
//        this.videoList = videoList;
//        this.context = context;
//
//    // load more
//        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                totalItemCount = linearLayoutManager.getItemCount();
//                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                    if (onLoadMoreListener != null) {
//                        onLoadMoreListener.onLoadMore();
//                    }
//                    isLoading = true;
//                }
//            }
//        });
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == VIEW_TYPE_ITEM) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video, parent, false);
//            return new ViewHolderRow(view);
//        } else if (viewType == VIEW_TYPE_LOADING) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_loading, parent, false);
//            return new ViewHolderLoading(view);
//        }
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//        if (holder instanceof ViewHolderRow) {
//            VideoFeed item = videoList.get(position);
//
//            ViewHolderRow userViewHolder = (ViewHolderRow) holder;
//
//            userViewHolder.content.setupMediaPlayer(item.getDc_video_content());
//            userViewHolder.page_number.setText(item.getIs_video_title());
//            userViewHolder.title.setText(item.getIs_video_title());
//
//            // binding item click listner
//            userViewHolder.bind(videoList.get(position), listener);
//        } else if (holder instanceof ViewHolderLoading) {
//            ViewHolderLoading loadingViewHolder = (ViewHolderLoading) holder;
//            loadingViewHolder.progressBar.setIndeterminate(true);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return videoList == null ? 0 : videoList.size();
//    }
//
//    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
//        this.onLoadMoreListener = mOnLoadMoreListener;
//    }
//
//    public void setOnItemListener(OnItemClickListener listener) {
//        this.listener = listener;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return videoList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
//    }
//
//    public void setLoaded() {
//        isLoading = false;
//    }
//
//    private class ViewHolderLoading extends RecyclerView.ViewHolder {
//        public ProgressBar progressBar;
//
//        public ViewHolderLoading(View view) {
//            super(view);
//            progressBar = (ProgressBar) view.findViewById(R.id.itemProgressbar);
//        }
//    }
//
//    private class ViewHolderRow extends RecyclerView.ViewHolder {
//        public final View mView;
//        public final TextView title, page_number;
//        public final FullscreenVideoView content;
//
//        public ViewHolderRow(View view) {
//            super(view);
//            mView = view;
//            title = (TextView) view.findViewById(R.id.titles);
//            page_number = (TextView) view.findViewById(R.id.pages);
//            content = (FullscreenVideoView) view.findViewById(R.id.fullscreenVideoView);
//
//        }
//
//        public void bind(final VideoFeed item, final OnItemClickListener listener) {
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onItemClick(item);
//                }
//            });
//        }
//
//    }
//}


public class VideoViewAdapter extends RecyclerView.Adapter<VideoViewAdapter.VideoViewHolder> {
    private List<VideoFeed> videoList;
//    private final AdapterListener<VideoFeed> mListener;
    Context context;

    public VideoViewAdapter(List<VideoFeed> videoList,Context context ) {
        this.videoList = videoList;
        this.context=context;
//        this.mListener = mListener;
    }

    @NonNull
    @Override
    public VideoViewAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video, parent, false);
        VideoViewHolder holder = new VideoViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewAdapter.VideoViewHolder holder, int position) {
        VideoFeed item = videoList.get(position);
        holder.content.setupMediaPlayer(item.getDc_video_content());
        holder.title.setText(item.getIs_video_title());
        holder.page_number.setText(item.getDc_video_page());

    }

    public void setVideoList(List<VideoFeed> videoList) {
        if (videoList == null) return;
        this.videoList.clear();

        this.videoList = videoList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView title, page_number;
        public final FullscreenVideoView content;

        public VideoViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.titles);
            page_number = (TextView) view.findViewById(R.id.pages);
            content = (FullscreenVideoView) view.findViewById(R.id.fullscreenVideoView);
        }
    }

//    class VideoTask extends AsyncTask<VideoFeed,Void, Bitmap>{
//
//        @Override
//        protected Bitmap doInBackground(VideoFeed... videoFeeds) {
//            return null;
//        }
//    }

}



