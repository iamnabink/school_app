package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Madhusudan Sapkota on 5/23/2018.
 */
@Entity(tableName = "class_routine")
public class ClassRoutine {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "period")
    @SerializedName("period")
    @Expose
    private String period;


    @ColumnInfo(name = "teacher_name")
    @SerializedName("teacher")
    @Expose
    private String teacher;


    @ColumnInfo(name = "start_time")
    @SerializedName("start_time")
    @Expose
    private String startTime;


    @ColumnInfo(name = "end_time")
    @SerializedName("end_time")
    @Expose
    private String endTime;


    @ColumnInfo(name = "subject_name")
    @SerializedName("subject")
    @Expose
    private String subject;

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
