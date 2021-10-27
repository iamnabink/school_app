package com.swipecrafts.school.data.model.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.swipecrafts.school.ui.dashboard.attendance.model.StudentAttendance;

import java.util.ArrayList;
import java.util.List;

public class AttendanceResponse {

    @NonNull
    @SerializedName("status")
    @Expose
    private Long mStatus;

    @Nullable
    @SerializedName("attendance_record")
    @Expose
    private List<StudentAttendance> mAttendanceRecord = new ArrayList<>();

    @Nullable
    @SerializedName("std_record")
    @Expose
    private List<StudentAttendance> mAttendanceStudents = new ArrayList<>();

    @NonNull
    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(@NonNull Long mStatus) {
        this.mStatus = mStatus;
    }

    @NonNull
    public List<StudentAttendance> getAttendanceRecord() {
        return mAttendanceRecord;
    }

    public void setAttendanceRecord(@Nullable List<StudentAttendance> mAttendanceRecord) {
        this.mAttendanceRecord = mAttendanceRecord;
    }

    @NonNull
    public List<StudentAttendance> getAttendanceStudents() {
        return mAttendanceStudents;
    }

    public void setAttendanceStudents(@Nullable List<StudentAttendance> mAttendanceStudents) {
        this.mAttendanceStudents = mAttendanceStudents;
    }
}
