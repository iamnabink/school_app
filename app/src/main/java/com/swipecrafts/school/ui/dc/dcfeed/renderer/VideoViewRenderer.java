package com.swipecrafts.school.ui.dc.dcfeed.renderer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swipecrafts.school.R;
import com.swipecrafts.school.ui.dc.dcfeed.ContentFeed;
import com.swipecrafts.school.utils.renderer.BaseViewRenderer;
import com.swipecrafts.school.utils.renderer.ClickListener;

/**
 * Created by Madhusudan Sapkota on 3/22/2018.
 */

public class VideoViewRenderer extends BaseViewRenderer<ContentFeed, VideoViewRenderer.ViewHolder>{

    private Context mContext;

    public VideoViewRenderer(Context context, int mType, ClickListener<ContentFeed> listener) {
        super(mType, listener);
        this.mContext = context;
    }

    @Override
    public void bindView(@NonNull ContentFeed model, @NonNull ViewHolder holder) {
        holder.model = model;
        holder.bindView(model);

        if (getClickListener() != null){
            // handel click events
        }
    }

    @NonNull
    @Override
    public ViewHolder createViewHolder(@Nullable ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_feed_item, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ContentFeed model;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
        }

        public void bindView(ContentFeed model) {

        }
    }
}
