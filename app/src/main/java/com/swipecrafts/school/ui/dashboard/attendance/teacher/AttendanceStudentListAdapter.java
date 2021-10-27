package com.swipecrafts.school.ui.dashboard.attendance.teacher;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.ui.dashboard.attendance.model.StudentAttendance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/30/2018.
 */
public class AttendanceStudentListAdapter extends RecyclerView.Adapter<AttendanceStudentListAdapter.ViewHolder> {

    private Context mContext;
    private final AttendanceListener attendanceListener;

    private List<StudentAttendance> studentList;
    private HashMap<Long, StudentAttendance> attendanceData;
    private boolean isAllStudentPresent = false;

    public AttendanceStudentListAdapter(Context mContext, List<StudentAttendance> studentList, AttendanceListener attendanceListener) {
        this.mContext = mContext;
        this.studentList = studentList;
        this.attendanceListener = attendanceListener;
        this.attendanceData = new HashMap<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.student = studentList.get(position);
        holder.studentNameTV.setText(holder.student.getName());
        holder.presentChkBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                holder.absentChkBtn.setChecked(false);
                holder.student.setStatus(1);
                attendanceData.put(holder.student.getStudentId(), holder.student);
            } else {
                if (holder.student.getStatus() == 0) {

                } else {
                    Log.e("Attendance", "Present removed!");
                    attendanceData.remove(holder.student.getStudentId());
                }
            }
            Log.e("Attendance", "Present " + isChecked + " total Attendance " + attendanceData.size());
            attendanceListener.onAttendance(getItemCount() == attendanceData.size());
        });

        holder.absentChkBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                holder.presentChkBtn.setChecked(false);
                holder.student.setStatus(0);
                attendanceData.put(holder.student.getStudentId(), holder.student);
            } else {
                if (holder.student.getStatus() == 1) {

                } else {
                    Log.e("Attendance", "Absent removed!");
                    attendanceData.remove(holder.student.getStudentId());
                }
            }

            Log.e("Attendance", "Absent " + isChecked + " total Attendance " + attendanceData.size());
            attendanceListener.onAttendance(getItemCount() == attendanceData.size());
        });

        holder.presentChkBtn.setChecked(isAllStudentPresent);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void updateStudentList(List<StudentAttendance> students) {
        this.studentList = students;
        notifyDataSetChanged();
    }

    public void setAllStudentPresent(boolean allStudentPresent) {
        isAllStudentPresent = allStudentPresent;
        attendanceData.clear();
        notifyDataSetChanged();
    }

    public List<StudentAttendance> getAttendanceData() {
        return new ArrayList<>(attendanceData.values());
    }

    interface AttendanceListener {
        void onAttendance(boolean isAttendanceCompleted);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView studentNameTV;
        private final CheckBox presentChkBtn;
        private final CheckBox absentChkBtn;
        private View view;
        private StudentAttendance student;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;

            studentNameTV = (TextView) view.findViewById(R.id.studentNameTV);
            presentChkBtn = (CheckBox) view.findViewById(R.id.chkPresent);
            absentChkBtn = (CheckBox) view.findViewById(R.id.chkAbsent);
        }
    }
}
