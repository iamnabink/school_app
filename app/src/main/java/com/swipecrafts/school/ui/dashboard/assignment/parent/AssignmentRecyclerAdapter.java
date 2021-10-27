package com.swipecrafts.school.ui.dashboard.assignment.parent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.ui.dashboard.assignment.model.Assignment;
import com.swipecrafts.school.utils.listener.AdapterListener;

import java.util.List;


public class AssignmentRecyclerAdapter extends RecyclerView.Adapter<AssignmentRecyclerAdapter.ViewHolder> {

    private final AdapterListener<Assignment> listener;
    private List<Assignment> assignments;

    public AssignmentRecyclerAdapter(List<Assignment> items, AdapterListener<Assignment> listener) {
        assignments = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_assignment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.model = assignments.get(position);

        holder.bindView();
        holder.btnOpenDocument.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClicked(holder.model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public void updateAssignmentList(List<Assignment> assignments) {
        this.assignments = assignments;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private final TextView subjectTV;
        private final TextView titleTV;
        private final TextView typeTV;
        private final TextView uploadedAtTV;
        private final Button btnOpenDocument;
        public Assignment model;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            subjectTV = (TextView) view.findViewById(R.id.assignmentSubjectTV);
            titleTV = (TextView) view.findViewById(R.id.assignmentTitleTV);
            typeTV = (TextView) view.findViewById(R.id.assignmentTypeTV);
            uploadedAtTV = (TextView) view.findViewById(R.id.assignmentUploadedDateTV);
            btnOpenDocument = (Button) view.findViewById(R.id.btnOpenFile);
        }

        public void bindView() {
            subjectTV.setText(model.getSubjectName());
            titleTV.setText(model.getTitle());
            typeTV.setText(model.getFileType());
            uploadedAtTV.setText(model.getUploadedAt());
        }
    }
}
