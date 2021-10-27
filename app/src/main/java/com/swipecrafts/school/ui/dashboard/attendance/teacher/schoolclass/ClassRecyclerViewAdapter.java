package com.swipecrafts.school.ui.dashboard.attendance.teacher.schoolclass;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.SchoolClass;
import com.swipecrafts.school.utils.listener.AdapterListener;

import java.util.List;


public class ClassRecyclerViewAdapter extends RecyclerView.Adapter<ClassRecyclerViewAdapter.ViewHolder> {

    private final AdapterListener<SchoolClass> mListener;
    private List<SchoolClass> schoolClassList;

    public ClassRecyclerViewAdapter(List<SchoolClass> items, AdapterListener<SchoolClass> listener) {
        schoolClassList = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.model = schoolClassList.get(position);
        holder.mItemTV.setText(holder.model.getClassName());

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onItemClicked(holder.model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return schoolClassList.size();
    }

    public void updateClasses(List<SchoolClass> data) {
        this.schoolClassList = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mItemTV;
        public SchoolClass model;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mItemTV = (TextView) view.findViewById(R.id.itemTextView);
        }
    }
}
