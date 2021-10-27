package com.swipecrafts.school.ui.dashboard.schedule;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.library.calendar.core.ADCalendar;
import com.swipecrafts.school.ui.dashboard.schedule.model.Day;
import com.swipecrafts.school.ui.dashboard.schedule.model.Routine;
import com.swipecrafts.school.utils.listener.AdapterListener;

import java.util.LinkedHashMap;
import java.util.List;

public class ClassRoutineListAdapter extends RecyclerView.Adapter<ClassRoutineListAdapter.ViewHolder> {

    private List<Day> dayList;
    private final AdapterListener<Routine> mListener;

    private LinkedHashMap<Integer, Pair<Boolean, Routine>> data;
    private SparseArray<String> dayHistory;

    public ClassRoutineListAdapter(List<Day> days, AdapterListener<Routine> listener) {
        this.dayList = days;
        mListener = listener;
        data = new LinkedHashMap<>();
        dayHistory = new SparseArray<>();

        initRoutineData();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_schedule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Pair<Boolean, Routine> pairData = data.get(position);
        boolean isFirst =  pairData.first;
        Routine routine = pairData.second;
        String dayName = dayHistory.get(position);

        if (isFirst){
            holder.dayNameTV.setText(dayName);
            holder.dayNameTV.setVisibility(View.VISIBLE);
        } else {
            holder.dayNameTV.setVisibility(View.GONE);
        }

        ADCalendar cal = ADCalendar.today();
        if (dayName.equalsIgnoreCase(cal.daysOfWeeks()[cal.getDayOfWeek()])){
            holder.activeDayIV.setVisibility(View.VISIBLE);
        }else {
            holder.activeDayIV.setVisibility(View.GONE);
        }


        holder.model = routine;
        holder.subjectNameTV.setText(routine.getSubject());
        holder.teacherNameTV.setText(routine.getTeacher());
        holder.classPeriodTV.setText(routine.getPeriod());
        holder.classStartTimeTV.setText(routine.getStartTime());
        holder.classEndTimeTV.setText(routine.getEndTime());

    }

    private void initRoutineData() {
        int position = 0;
        for (Day day : dayList) {
            boolean isFirst = true;

            for (Routine routine: day.getRoutine()) {
                data.put(position, new Pair<>(isFirst, routine));
                dayHistory.put(position, day.getDayName());
                position++;
                isFirst = false;
            }
        }
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateRoutineDetails(List<Day> days) {
        this.dayList = days;
        initRoutineData();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Routine model;
        private View view;

        private TextView dayNameTV;
        private TextView subjectNameTV;
        private TextView teacherNameTV;
        private TextView classPeriodTV;
        private TextView classStartTimeTV;
        private TextView classEndTimeTV;

        private ImageView activeDayIV;

        public ViewHolder(View view) {
            super(view);
            this.view = itemView;

            dayNameTV = (TextView) view.findViewById(R.id.dayNameTV);
            subjectNameTV = (TextView) view.findViewById(R.id.subjectNameTV);
            teacherNameTV = (TextView) view.findViewById(R.id.teacherNameTV);
            classPeriodTV = (TextView) view.findViewById(R.id.classPeriodTV);
            classStartTimeTV = (TextView) view.findViewById(R.id.classStartTV);
            classEndTimeTV = (TextView) view.findViewById(R.id.classEndTV);

            activeDayIV = (ImageView) view.findViewById(R.id.activeDayImageView);
        }
    }
}
