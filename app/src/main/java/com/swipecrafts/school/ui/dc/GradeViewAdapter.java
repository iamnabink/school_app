package com.swipecrafts.school.ui.dc;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.Grade;
import com.swipecrafts.library.views.CircleImageView;
import com.swipecrafts.school.utils.listener.AdapterListener;
import com.swipecrafts.school.utils.listener.ImageLoader;

import java.util.List;

public class GradeViewAdapter extends RecyclerView.Adapter<GradeViewAdapter.ViewHolder> {

    private List<Grade> mValues;
    private final AdapterListener<Grade> mListener;
    private ImageLoader<Grade> mImageLoader;

    public GradeViewAdapter(List<Grade> items, ImageLoader<Grade> imageLoader, AdapterListener<Grade> listener) {
        mValues = items;
        mListener = listener;
        this.mImageLoader = imageLoader;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dc_grade_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Grade item = mValues.get(position);
        holder.model = item;

        holder.gradeNameTV.setText(item.getDcGradeName());
        mImageLoader.loadImage(holder.gradeImageView, holder.model);

        holder.mView.setOnClickListener(view -> {
            if (null != mListener) {
                mListener.onItemClicked(holder.model);
            }
        });
    }

    public void setValues(List<Grade> mValues) {
        this.mValues.clear();
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Grade model;

        public final TextView gradeNameTV;
        public final ImageView gradeImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            gradeNameTV = (TextView) view.findViewById(R.id.gradeTitleTV);
            gradeImageView = (CircleImageView) view.findViewById(R.id.gradeBackgroundImgV);
        }
    }
}
