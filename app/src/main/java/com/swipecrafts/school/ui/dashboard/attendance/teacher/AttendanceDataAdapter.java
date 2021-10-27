package com.swipecrafts.school.ui.dashboard.attendance.teacher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.ui.dashboard.attendance.model.StudentAttendance;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 5/28/2018.
 */
public class AttendanceDataAdapter extends RecyclerView.Adapter<AttendanceDataAdapter.ViewHolder> {

    private final Context context;
    private List<StudentAttendance> attendanceData;

   private final Drawable presentDrawable;
   private final Drawable absentDrawable;
   private final String presentText;
   private final String absentText;
   private final @ColorInt int presentColor;
   private final @ColorInt int absentColor;

    public AttendanceDataAdapter(Context context, List<StudentAttendance> attendanceData) {
        this.context = context;
        this.attendanceData = attendanceData;

        this.presentDrawable = context.getResources().getDrawable(R.drawable.present_chk_selector);
        this.absentDrawable = context.getResources().getDrawable(R.drawable.absent_chk_selector);
        this.presentText = context.getResources().getString(R.string.present);
        this.absentText = context.getResources().getString(R.string.absent);
        this.presentColor = context.getResources().getColor(R.color.componentPrimaryColor);
        this.absentColor = context.getResources().getColor(R.color.colorHighlightText);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_data_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.model = attendanceData.get(position);

        holder.studentNameTV.setText(holder.model.getName());

        boolean present = holder.model.getStatus() == 1;
        holder.attendanceChk.setButtonDrawable(present ? presentDrawable : absentDrawable);
        holder.attendanceLbl.setText(present ? presentText : absentText);
        holder.attendanceLbl.setTextColor(holder.model.getStatus() == 1 ? presentColor : absentColor);
    }

    public void updateAttendanceData(List<StudentAttendance> attendanceList){
        this.attendanceData = attendanceList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return attendanceData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public StudentAttendance model;
        public final TextView studentNameTV;
        public final CheckBox attendanceChk;
        public final TextView attendanceLbl;

        public ViewHolder(View itemView) {
            super(itemView);

            studentNameTV = (TextView) itemView.findViewById(R.id.studentNameTV);
            attendanceChk = (CheckBox) itemView.findViewById(R.id.chkAttendance);
            attendanceLbl = (TextView) itemView.findViewById(R.id.attendanceLbl);
        }
    }
}
