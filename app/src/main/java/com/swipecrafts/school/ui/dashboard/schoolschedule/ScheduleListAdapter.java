package com.swipecrafts.school.ui.dashboard.schoolschedule;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.SchoolSchedule;
import com.swipecrafts.school.utils.listener.AdapterListener;

import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {

    private List<SchoolSchedule> mScheduleList;
    private final AdapterListener<SchoolSchedule> mListener;

    public ScheduleListAdapter(List<SchoolSchedule> items, AdapterListener<SchoolSchedule> listener) {
        mScheduleList = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.model = mScheduleList.get(position);
        holder.bindView();
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onItemClicked(holder.model);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mScheduleList.size();
    }

    public void updateScheduleItems(List<SchoolSchedule> list) {
        this.mScheduleList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView mDayNameTV;
        public final TextView arrivalTimeTV;
        public final TextView earlyAssemblyTV;
        public final TextView classStartTV;
        public final TextView lunchTimeTV;
        public final TextView lunchEndTime;
        public final TextView schoolEndTV;
        public final TextView lateAssemblyTV;

        public SchoolSchedule model;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mDayNameTV = (TextView) view.findViewById(R.id.dayNameTV);
            arrivalTimeTV = (TextView) view.findViewById(R.id.arrivalTimeTV);
            earlyAssemblyTV = (TextView) view.findViewById(R.id.earlyAssemblyTimeTV);
            classStartTV = (TextView) view.findViewById(R.id.classStartTimeTV);
            lunchTimeTV = (TextView) view.findViewById(R.id.lunchBreakStartTimeTV);
            lunchEndTime = (TextView) view.findViewById(R.id.lunchBreakEndTimeTV);
            schoolEndTV = (TextView) view.findViewById(R.id.schoolBreakTimeTV);
            lateAssemblyTV = (TextView) view.findViewById(R.id.lateAssemblyTimeTV);
        }

        public void bindView() {

            mDayNameTV.setText(model.getDay());
            arrivalTimeTV.setText(model.getArrivalTime());
            earlyAssemblyTV.setText(model.getAssemblyTime());
            classStartTV.setText(model.getClassStartFrom());
            lunchTimeTV.setText(model.getLunchBreakTime());
            lunchEndTime.setText(model.getAfterBreakClassTime());
            schoolEndTV.setText(model.getSchoolEndTime());
            lateAssemblyTV.setText(model.getEndAssemblyTime());
        }
    }
}
