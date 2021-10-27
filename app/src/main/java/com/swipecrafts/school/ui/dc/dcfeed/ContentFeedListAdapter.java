package com.swipecrafts.school.ui.dc.dcfeed;

import android.annotation.SuppressLint;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.swipecrafts.library.youtube.YTPlayerFragmentManager;
import com.swipecrafts.school.ui.dc.dcfeed.renderer.ImageViewRenderer;
import com.swipecrafts.school.ui.dc.dcfeed.renderer.PDFViewRenderer;
import com.swipecrafts.school.ui.dc.dcfeed.renderer.TextViewRenderer;
import com.swipecrafts.school.ui.dc.dcfeed.renderer.VideoViewRenderer;
import com.swipecrafts.school.ui.dc.dcfeed.renderer.YTVideoViewRenderer;
import com.swipecrafts.school.utils.renderer.BaseViewRenderer;
import com.swipecrafts.school.utils.renderer.ClickListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class ContentFeedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int IMAGE = 0;
    public static final int YTD_VIDEO = 1;
    public static final int VIDEO = 2;
    public static final int PDF = 3;
    public static final int TEXT = 4;
    public static final int AUDIO = 5;

    private final ClickListener<ContentFeed> mListener;

    @NonNull
    private final SparseArray<BaseViewRenderer> mRendererList = new SparseArray<>();
    private List<ContentFeed> contentFeedList;
    private YTPlayerFragmentManager mFragment;


    public ContentFeedListAdapter(YTPlayerFragmentManager fragment, List<ContentFeed> contentFeedList, ClickListener<ContentFeed> mListener) {
        this.mListener = mListener;
        this.mFragment = fragment;
        this.contentFeedList = contentFeedList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("viewType", viewType + "");
        BaseViewRenderer renderer = mRendererList.get(viewType);
        if (renderer == null) {
            renderer = getViewRenderer(viewType);
            mRendererList.put(viewType, renderer);
        }
        return renderer.createViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ContentFeed item = contentFeedList.get(position);
        final BaseViewRenderer renderer = mRendererList.get(item.getType());
        renderer.bindView(item, holder);

    }

    @Override
    public int getItemViewType(int position) {
        ContentFeed contentFeed = contentFeedList.get(position);
        return contentFeed.getType();
    }

    @Override
    public int getItemCount() {
        return contentFeedList.size();
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        int position = holder.getAdapterPosition();
        if (position < 0) return;
        if (holder instanceof YTVideoViewRenderer.ViewHolder) {
            YTVideoViewRenderer.ViewHolder VH = (YTVideoViewRenderer.ViewHolder) holder;
            VH.unBindView(mFragment);
        }
    }

    public void updateContentFeeds(List<ContentFeed> contentFeeds) {
        this.contentFeedList = contentFeeds;
        notifyDataSetChanged();
    }

    private BaseViewRenderer getViewRenderer(@ContentFeedType int type) {
        switch (type) {
            case VIDEO:
                return new VideoViewRenderer(mFragment.getContext(), type, mListener);
            case YTD_VIDEO:
                return new YTVideoViewRenderer(mFragment, type, mListener);
            case IMAGE:
                return new ImageViewRenderer(mFragment.getContext(), type, mListener);
            case PDF:
                return new PDFViewRenderer(mFragment.getContext(), type, mListener);
            case TEXT:
                return new TextViewRenderer(mFragment.getContext(), type, mListener);
            default:
                return new TextViewRenderer(mFragment.getContext(), type, mListener);
        }
    }

    @SuppressLint("UniqueConstants")
    @Retention(RetentionPolicy.RUNTIME)
    @IntDef(flag = true, value = {IMAGE, YTD_VIDEO, VIDEO, AUDIO, PDF, TEXT})
    @interface ContentFeedType {
    }
}
