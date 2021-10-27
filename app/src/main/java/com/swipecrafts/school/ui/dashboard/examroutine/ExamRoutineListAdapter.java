package com.swipecrafts.school.ui.dashboard.examroutine;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.ui.dashboard.examroutine.model.ExamRoutine;
import com.swipecrafts.school.ui.dashboard.examroutine.model.ExamRoutineRes;

import java.util.LinkedHashMap;
import java.util.List;

public class ExamRoutineListAdapter extends RecyclerView.Adapter<ExamRoutineListAdapter.ViewHolder> {

    private List<ExamRoutineRes> examRoutineResRoutineList;

    private LinkedHashMap<Integer, Pair<Integer, ExamRoutineRes>> data;


    public ExamRoutineListAdapter(List<ExamRoutineRes> examRoutineResRoutineList) {
        this.examRoutineResRoutineList = examRoutineResRoutineList;
        data = new LinkedHashMap<>();
        initRoutineData();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_schedule_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Pair<Integer, ExamRoutineRes> pairData = data.get(position);
        boolean isFirst = pairData.first == 0;
        ExamRoutineRes examRoutineRes = pairData.second;
        ExamRoutine routine = examRoutineRes.getRoutine().get(pairData.first);

        if (isFirst) {
            holder.examTermTV.setText(examRoutineRes.getExamType());
            holder.examYearTV.setText(examRoutineRes.getYear());
            holder.examYearTV.setVisibility(View.VISIBLE);
            holder.examTermTV.setVisibility(View.VISIBLE);
        } else {
            holder.examTermTV.setVisibility(View.GONE);
            holder.examYearTV.setVisibility(View.VISIBLE);
        }

        holder.model = routine;

        holder.subjectNameTV.setText(holder.model.getSubjectName());
        holder.dayNameTV.setText(holder.model.getDayName());
        holder.examDurationTV.setText(holder.model.getDuration());
        holder.examTimeTV.setText(holder.model.getTime());
    }

    private void initRoutineData() {
        int position = 0;
        for (ExamRoutineRes examRoutineRes : examRoutineResRoutineList) {
            for (int i = 0; i < examRoutineRes.getRoutine().size(); i++) {
                data.put(position, new Pair<>(i, examRoutineRes));
                position++;
            }

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateRoutineDetails(List<ExamRoutineRes> examRoutineResRoutineList) {
        this.examRoutineResRoutineList = examRoutineResRoutineList;
        initRoutineData();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ExamRoutine model;
        private View view;

        private TextView examTermTV;
        private TextView examYearTV;
        private TextView subjectNameTV;
        private TextView dayNameTV;
        private TextView examDurationTV;
        private TextView examTimeTV;

        public ViewHolder(View view) {
            super(view);
            this.view = itemView;

            examTermTV = (TextView) view.findViewById(R.id.examTermTV);
            examYearTV = (TextView) view.findViewById(R.id.examYearTV);
            dayNameTV = (TextView) view.findViewById(R.id.dayNameTV);
            subjectNameTV = (TextView) view.findViewById(R.id.subjectNameTV);
            examDurationTV = (TextView) view.findViewById(R.id.examDurationTV);
            examTimeTV = (TextView) view.findViewById(R.id.examTimeTV);
        }
    }
}
