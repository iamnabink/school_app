package com.swipecrafts.school.ui.dc.dcfeed.renderer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.swipecrafts.school.R;
import com.swipecrafts.library.youtube.YTPlayerFragmentManager;
import com.swipecrafts.school.ui.dc.dcfeed.ContentFeed;
import com.swipecrafts.school.utils.renderer.BaseViewRenderer;
import com.swipecrafts.school.utils.renderer.ClickListener;

import java.text.DateFormat;
import java.util.HashMap;

/**
 * Created by Madhusudan Sapkota on 3/22/2018.
 */

public class YTVideoViewRenderer extends BaseViewRenderer<ContentFeed, YTVideoViewRenderer.ViewHolder> {
    private YTPlayerFragmentManager mContext;

    private HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader> mThumbnailViewToLoaderMap;
    private SparseArray<YouTubePlayerSupportFragment> ytFragmentList;


    public YTVideoViewRenderer(YTPlayerFragmentManager context, int mType, ClickListener<ContentFeed> listener) {
        super(mType, listener);
        this.mContext = context;
        this.mThumbnailViewToLoaderMap = new HashMap<>();
        this.ytFragmentList = new SparseArray<>();
    }

    @Override
    public void bindView(@NonNull ContentFeed model, @NonNull YTVideoViewRenderer.ViewHolder holder) {
        holder.model = model;

        holder.contentTitleTV.setText(model.getDcContentTitle());
        holder.contentDescTV.setText(model.getDcContentDesc());

        String date = DateFormat.getDateInstance(DateFormat.LONG).format(model.getDcContentPostedOn());
        holder.contentDateTV.setText(date);

        holder.videoPlayImgBtn.setOnClickListener(v -> {
            if (mContext != null) {
                mContext.preparePlayer(holder.videoContainer, model.getDcContentLinkKey(), holder.getAdapterPosition());
            }
        });

        YouTubeThumbnailView videoThumb = holder.youTubeThumbnailView;
        YouTubeThumbnailLoader loader = mThumbnailViewToLoaderMap.get(videoThumb);

        if (loader == null) {
            videoThumb.setTag(holder.model.getDcContentLinkKey());

            videoThumb.initialize(mContext.getContext().getString(R.string.YOUTUBE_DEVELOPER_KEY), new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(holder.model.getDcContentLinkKey());
                    mThumbnailViewToLoaderMap.put(videoThumb, youTubeThumbnailLoader);
                    holder.progressBar.setVisibility(View.GONE);
                    holder.videoPlayImgBtn.setVisibility(View.VISIBLE);
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.videoPlayImgBtn.setVisibility(View.VISIBLE);
                }
            });
        } else {
            videoThumb.setImageResource(R.drawable.ic_spinner);
            loader.setVideo(holder.model.getDcContentLinkKey());
        }
    }

    @NonNull
    @Override
    public ViewHolder createViewHolder(@Nullable ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_yt_feed_item, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final YouTubeThumbnailView youTubeThumbnailView;
        public final ImageButton videoPlayImgBtn;
        public final ProgressBar progressBar;
        public final FrameLayout videoContainer;
        public final TextView contentTitleTV;
        public final TextView contentDescTV;
        public final TextView contentDateTV;
        public ContentFeed model;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;

            youTubeThumbnailView = (YouTubeThumbnailView) view.findViewById(R.id.youtubeThumbnailView);
            videoPlayImgBtn = (ImageButton) view.findViewById(R.id.play_button);
            progressBar = (ProgressBar) view.findViewById(R.id.thumbnailProgress);

            videoContainer = (FrameLayout) view.findViewById(R.id.video_container);

            contentTitleTV = (TextView) view.findViewById(R.id.contentTitleTV);
            contentDescTV = (TextView) view.findViewById(R.id.contentDescTV);
            contentDateTV = (TextView) view.findViewById(R.id.contentTimeTV);
        }

        public void unBindView(YTPlayerFragmentManager manager) {
            manager.unBindPlayer(videoContainer);
        }
    }
}


