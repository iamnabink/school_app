package com.swipecrafts.school.ui.dc.dcfeed.renderer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.ui.dc.dcfeed.ContentFeed;
import com.swipecrafts.school.utils.renderer.BaseViewRenderer;
import com.swipecrafts.school.utils.renderer.ClickListener;

import java.text.DateFormat;

/**
 * Created by Madhusudan Sapkota on 3/22/2018.
 */

public class TextViewRenderer extends BaseViewRenderer<ContentFeed, TextViewRenderer.ViewHolder>{


    private Context mContext;

    public TextViewRenderer(Context context, int mType, ClickListener<ContentFeed> listener) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_text_feed_item, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ContentFeed model;

        private final TextView contentTitleTV;
        private final TextView contentDescTV;
        private final TextView contentDateTV;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;

            contentTitleTV = (TextView) view.findViewById(R.id.contentTitleTV);
            contentDescTV = (TextView) view.findViewById(R.id.contentDescTV);
            contentDateTV = (TextView) view.findViewById(R.id.contentTimeTV);
        }

        public void bindView(ContentFeed model) {
            contentTitleTV.setText(model.getDcContentTitle());
            String desc = model.getDcContentDesc();
            if (TextUtils.isEmpty(desc)) desc = model.getDcContentName();

            contentDescTV.setText(desc);

//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            String time = sdf.format(model.getDcContentPostedOn());
            String time = DateFormat.getDateInstance(DateFormat.LONG).format(model.getDcContentPostedOn());
            contentDateTV.setText(time);
        }
    }
}
