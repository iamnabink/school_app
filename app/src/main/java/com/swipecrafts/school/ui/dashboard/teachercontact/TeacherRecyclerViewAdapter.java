package com.swipecrafts.school.ui.dashboard.teachercontact;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.Teacher;
import com.swipecrafts.school.utils.listener.AdapterListener;

import java.util.List;

public class TeacherRecyclerViewAdapter extends RecyclerView.Adapter<TeacherRecyclerViewAdapter.ViewHolder> {

    private final AdapterListener<Teacher> mListener;
    private List<Teacher> teacherList;

    public TeacherRecyclerViewAdapter(List<Teacher> items, AdapterListener<Teacher> listener) {
        teacherList = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.model = teacherList.get(position);
        holder.updateUI();

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onItemClicked(holder.model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public void setItems(List<Teacher> items) {
        this.teacherList = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txtName;
        public final TextView txtPhone;
        public final TextView txtEmail;
        public Teacher model;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtName = (TextView) view.findViewById(R.id.teacherName);
            txtPhone = (TextView) view.findViewById(R.id.teacherContact);
            txtEmail = (TextView) view.findViewById(R.id.teacherEmail);
        }

        public void updateUI() {
            txtName.setText(model.getName());
            txtPhone.setText(model.getPhone());
            txtEmail.setText(model.getEmail());
        }
    }
}
