package com.swipecrafts.school.ui.dc.subject;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.DCSubject;
import com.swipecrafts.school.ui.dc.Video.VideoFragment;
import com.swipecrafts.school.utils.listener.AdapterListener;
import com.swipecrafts.school.utils.listener.ImageLoader;

import java.util.List;


public class SubjectViewAdapter extends RecyclerView.Adapter<SubjectViewAdapter.ViewHolder> {

    private List<DCSubject> mDCSubjectList;
    private final AdapterListener<DCSubject> mListener;
    private final ImageLoader<DCSubject> imageLoader;

    public SubjectViewAdapter(List<DCSubject> items, AdapterListener<DCSubject> listener, ImageLoader<DCSubject> imageLoader) {
        mDCSubjectList = items;
        mListener = listener;
        this.imageLoader = imageLoader;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DCSubject item = mDCSubjectList.get(position);
        holder.model = item;

        Log.d("getTitle",item.getDcSubjectName());
        holder.mSubjectName.setText(item.getDcSubjectName());
        imageLoader.loadImage(holder.mSubjectImgView, holder.model);
        holder.mView.setOnClickListener( it -> {
            if (null != mListener) {
                mListener.onItemClicked(holder.model);
            }
        });
    }

    public void updateSubjects(List<DCSubject> DCSubjectList) {
        this.mDCSubjectList.clear();
        this.mDCSubjectList = DCSubjectList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDCSubjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mSubjectName;
        public final ImageView mSubjectImgView;

        public DCSubject model;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSubjectName = (TextView) view.findViewById(R.id.subjectNameTV);
            mSubjectImgView = (ImageView) view.findViewById(R.id.subjectImageView);

        }
    }
}
