package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "school_schedule")
public class SchoolSchedule {

    //    {
// "day": "Monday",
//         "arrival_time": "10:00 AM",
//         "assembly_time": "09:00 AM",
//         "class_start_from": "10:00 AM",
//         "lunch_break_time": "12:30 AM",
//         "after_break": "12:00 AM",
//         "school_end_time": "12:00 AM",
//         "end_assembly_time": "03:35 PM"
//    }

    @PrimaryKey
    @NonNull
    @SerializedName("day")
    @Expose
    @ColumnInfo(name = "day_name")
    private String mDay;

    @SerializedName("arrival_time")
    @Expose
    @ColumnInfo(name = "arrival_time")
    private String mArrivalTime;

    @SerializedName("assembly_time")
    @Expose
    @ColumnInfo(name = "assembly_time")
    private String mAssemblyTime;


    @SerializedName("class_start_from")
    @Expose
    @ColumnInfo(name = "class_start_from")
    private String mClassStartFrom;

    @SerializedName("class_end_time")
    @Expose
    @ColumnInfo(name = "class_end_time")
    private String mClassEndTime;

    @SerializedName("lunch_break_time")
    @Expose
    @ColumnInfo(name = "lunch_break_time")
    private String mLunchBreakTime;

    @SerializedName("after_break")
    @Expose
    @ColumnInfo(name = "after_break")
    private String mAfterBreakClassTime;

    @SerializedName("school_end_time")
    @Expose
    @ColumnInfo(name = "school_end_time")
    private String mSchoolEndTime;

    @SerializedName("end_assembly_time")
    @Expose
    @ColumnInfo(name = "end_assembly_time")
    private String mEndAssemblyTime;


    public SchoolSchedule(@NonNull String mDay, String mArrivalTime, String mAssemblyTime, String mClassStartFrom, String mClassEndTime, String mLunchBreakTime, String mAfterBreakClassTime, String mSchoolEndTime, String mEndAssemblyTime) {
        this.mDay = mDay;
        this.mArrivalTime = mArrivalTime;
        this.mAssemblyTime = mAssemblyTime;
        this.mClassStartFrom = mClassStartFrom;
        this.mClassEndTime = mClassEndTime;
        this.mLunchBreakTime = mLunchBreakTime;
        this.mAfterBreakClassTime = mAfterBreakClassTime;
        this.mSchoolEndTime = mSchoolEndTime;
        this.mEndAssemblyTime = mEndAssemblyTime;
    }

    @NonNull
    public String getDay() {
        return mDay;
    }

    public void setDay(String mDay) {
        this.mDay = mDay;
    }

    public String getArrivalTime() {
        return mArrivalTime;
    }

    public void setArrivalTime(String mArrivalTime) {
        this.mArrivalTime = mArrivalTime;
    }

    public String getAssemblyTime() {
        return mAssemblyTime;
    }

    public void setAssemblyTime(String mAssemblyTime) {
        this.mAssemblyTime = mAssemblyTime;
    }

    public String getClassStartFrom() {
        return mClassStartFrom;
    }

    public void setClassStartFrom(String mClassStartFrom) {
        this.mClassStartFrom = mClassStartFrom;
    }

    public String getClassEndTime() {
        return mClassEndTime;
    }

    public void setClassEndTime(String mClassEndTime) {
        this.mClassEndTime = mClassEndTime;
    }

    public String getLunchBreakTime() {
        return mLunchBreakTime;
    }

    public void setLunchBreakTime(String mLunchBreakTime) {
        this.mLunchBreakTime = mLunchBreakTime;
    }

    public String getAfterBreakClassTime() {
        return mAfterBreakClassTime;
    }

    public void setAfterBreakClassTime(String mAfterBreakClassTime) {
        this.mAfterBreakClassTime = mAfterBreakClassTime;
    }

    public String getSchoolEndTime() {
        return mSchoolEndTime;
    }

    public void setSchoolEndTime(String mSchoolEndTime) {
        this.mSchoolEndTime = mSchoolEndTime;
    }

    public String getEndAssemblyTime() {
        return mEndAssemblyTime;
    }

    public void setEndAssemblyTime(String mEndAssemblyTime) {
        this.mEndAssemblyTime = mEndAssemblyTime;
    }
}
