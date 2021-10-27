package com.swipecrafts.school.ui.dc.dcfeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.swipecrafts.school.R;
import com.swipecrafts.library.views.OnlineDocumentViewer;
import com.swipecrafts.library.youtube.YTPlayerFragmentManager;
import com.swipecrafts.school.ui.dashboard.gallery.FullScreenImageGalleryActivity;
import com.swipecrafts.school.utils.listener.AdapterListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.swipecrafts.school.ui.dc.dcfeed.ContentFeedListAdapter.AUDIO;
import static com.swipecrafts.school.ui.dc.dcfeed.ContentFeedListAdapter.IMAGE;
import static com.swipecrafts.school.ui.dc.dcfeed.ContentFeedListAdapter.PDF;
import static com.swipecrafts.school.ui.dc.dcfeed.ContentFeedListAdapter.VIDEO;
import static com.swipecrafts.school.ui.dc.dcfeed.ContentFeedListAdapter.YTD_VIDEO;

/**
 * Created by Madhusudan Sapkota on 7/2/2018.
 */
public class DCFeedAdapter extends RecyclerView.Adapter<DCFeedAdapter.ViewHolder> {
    private YTPlayerFragmentManager mFragment;
    private List<ContentFeed> dcFeeds;
    private AdapterListener<ContentFeed> listener;
    private AudioHandler audioHandler;
    private HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader> mThumbnailViewToLoaderMap;

    public DCFeedAdapter(YTPlayerFragmentManager mFragment, List<ContentFeed> dcFeeds, AdapterListener<ContentFeed> listener, AudioHandler audioHandler) {
        this.mFragment = mFragment;
        this.dcFeeds = dcFeeds;
        this.listener = listener;
        this.audioHandler = audioHandler;
        this.mThumbnailViewToLoaderMap = new HashMap<>();
    }

    public void setDcFeeds(List<ContentFeed> dcFeeds) {
        this.mThumbnailViewToLoaderMap.clear();
        this.dcFeeds = dcFeeds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_audio_feed_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.model = dcFeeds.get(position);
        holder.bindGeneralItem();
        switch (holder.model.getType()) {
            case YTD_VIDEO:
                holder.imageContainer.setVisibility(View.GONE);
                holder.pdfContainer.setVisibility(View.GONE);
                holder.audioContainer.setVisibility(View.GONE);
                holder.youtubeContainer.setVisibility(View.VISIBLE);
                manageYoutubePlayer(holder);
                break;
            case VIDEO:
                holder.imageContainer.setVisibility(View.GONE);
                holder.pdfContainer.setVisibility(View.GONE);
                holder.audioContainer.setVisibility(View.GONE);
                holder.youtubeContainer.setVisibility(View.GONE);
                break;
            case AUDIO:
                holder.imageContainer.setVisibility(View.GONE);
                holder.pdfContainer.setVisibility(View.GONE);
                holder.audioContainer.setVisibility(View.VISIBLE);
                holder.youtubeContainer.setVisibility(View.GONE);
                manageAudioPlayer(holder);
                break;
            case PDF:
                holder.imageContainer.setVisibility(View.GONE);
                holder.pdfContainer.setVisibility(View.VISIBLE);
                holder.audioContainer.setVisibility(View.GONE);
                holder.youtubeContainer.setVisibility(View.GONE);
                managePdfView(holder);
                break;
            case IMAGE:
                holder.imageContainer.setVisibility(View.VISIBLE);
                holder.pdfContainer.setVisibility(View.GONE);
                holder.audioContainer.setVisibility(View.GONE);
                holder.youtubeContainer.setVisibility(View.GONE);
                manageImageView(holder);
                break;
            default:
                holder.imageContainer.setVisibility(View.GONE);
                holder.pdfContainer.setVisibility(View.GONE);
                holder.audioContainer.setVisibility(View.GONE);
                holder.youtubeContainer.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dcFeeds.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.model.getType() == YTD_VIDEO) {
            FrameLayout videoContainer = (FrameLayout) holder.view.findViewById(R.id.youtubeVideoPlayerFrame);
            mFragment.unBindPlayer(videoContainer);
        }
    }

    private void manageImageView(ViewHolder holder) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.progressbar)
                .error(R.drawable.placeholder);

        Glide.with(mFragment)
                .load(holder.model.getDcContentLinkKey())
                .apply(options)
                .into(holder.imageContainer);

        holder.imageContainer.setOnClickListener(v -> {
            Intent i = new Intent(mFragment.getActivity(), FullScreenImageGalleryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(FullScreenImageGalleryActivity.KEY_IMAGES, new ArrayList<String>(Arrays.asList(holder.model.getDcContentLinkKey())));
            bundle.putInt(FullScreenImageGalleryActivity.KEY_POSITION, 0);
            i.putExtras(bundle);
            mFragment.startActivity(i);
            mFragment.getActivity().overridePendingTransition(R.anim.side_in, R.anim.side_out);
        });
    }

    private void managePdfView(ViewHolder holder) {
        Button documentViewBtn = (Button) holder.view.findViewById(R.id.openDocumentBtn);

        documentViewBtn.setOnClickListener(v -> {
            Intent intent = new Intent(mFragment.getActivity(), OnlineDocumentViewer.class);
            Bundle bundle = new Bundle();
            bundle.putString(OnlineDocumentViewer.ARG_FILE_URL, holder.model.getDcContentLinkKey());
            bundle.putString(OnlineDocumentViewer.ARG_FILE_NAME, holder.model.getDcContentTitle());
            intent.putExtras(bundle);
            mFragment.startActivity(intent);
            mFragment.getActivity().overridePendingTransition(R.anim.side_in, R.anim.side_out);
        });
    }

    private void manageAudioPlayer(ViewHolder holder) {
        ImageButton btnPlayAudio = (ImageButton) holder.view.findViewById(R.id.audioPlayBtn);
        btnPlayAudio.setOnClickListener(v -> {
            if (audioHandler != null) {
                audioHandler.playAudio(holder.model, btnPlayAudio);
            }
        });


    }

    private void manageYoutubePlayer(ViewHolder holder) {
        ImageButton videoPlayBtn = (ImageButton) holder.view.findViewById(R.id.youtubePlayerBtn);
        YouTubeThumbnailView videoThumb = (YouTubeThumbnailView) holder.view.findViewById(R.id.youtubeThumbnailView);
        FrameLayout videoContainer = (FrameLayout) holder.view.findViewById(R.id.youtubeVideoPlayerFrame);
        videoContainer.setTag(R.string.video_component_tag);

        videoPlayBtn.setOnClickListener(v -> {
            if (mFragment != null) {
                mFragment.preparePlayer(videoContainer, holder.model.getDcContentLinkKey(), holder.getAdapterPosition());
            }
        });
        YouTubeThumbnailLoader loader = mThumbnailViewToLoaderMap.get(videoThumb);

        if (loader == null) {
            videoThumb.setTag(holder.model.getDcContentLinkKey());
            videoThumb.initialize(mFragment.getYoutubeDeveloperKey(), new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(holder.model.getDcContentLinkKey());
                    mThumbnailViewToLoaderMap.put(videoThumb, youTubeThumbnailLoader);
                    videoPlayBtn.setVisibility(View.VISIBLE);
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    videoPlayBtn.setVisibility(View.VISIBLE);
                }
            });
        } else {
            videoThumb.setImageResource(R.drawable.ic_spinner);
            loader.setVideo(holder.model.getDcContentLinkKey());
        }
    }

    public interface AudioHandler {
        void playAudio(ContentFeed model, ImageButton playBtn);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTV;
        private final TextView descTV;
        private final TextView dateTV,Pageno;
        private final ImageView imageContainer;
        private final RelativeLayout audioContainer;
        private final RelativeLayout youtubeContainer;
        private final RelativeLayout pdfContainer;
        public View view;
        public ContentFeed model;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;

            titleTV = (TextView) view.findViewById(R.id.feedTitleTV);
            descTV = (TextView) view.findViewById(R.id.feedDescTV);
            dateTV = (TextView) view.findViewById(R.id.feedDateTV);
            Pageno = view.findViewById(R.id.feedPage);

            imageContainer = (ImageView) view.findViewById(R.id.feedImageView);
            pdfContainer = (RelativeLayout) view.findViewById(R.id.pdfViewContainer);
            audioContainer = (RelativeLayout) view.findViewById(R.id.audioPlayerContainer);
            youtubeContainer = (RelativeLayout) view.findViewById(R.id.youtubePayerContainer);
        }

        public void bindGeneralItem() {
            String title = model.getDcContentTitle();
//            String Pageno = model.getDcContentPage();
            String desc = model.getDcContentDesc();
            Log.e("ContentData", desc);
            titleTV.setText(title == null ? "N/A" : title);
            if (desc == null) {
                descTV.setVisibility(View.GONE);
            } else {
                descTV.setVisibility(View.VISIBLE);
                descTV.setText(Html.fromHtml(desc));
            }
            String time = DateFormat.getDateInstance(DateFormat.LONG).format(model.getDcContentPostedOn());
            dateTV.setText(time);

        }
    }
}
