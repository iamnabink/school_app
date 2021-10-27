package com.swipecrafts.school.ui.dashboard.examroutine.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Madhusudan Sapkota on 4/6/2018.
 */
public class ExamRoutine {

    @SerializedName("subject")
    @Expose
    private String subjectName;

    @SerializedName("day")
    @Expose
    private String dayName;

    @SerializedName("start_time")
    @Expose
    private String time;

    @SerializedName("duration")
    @Expose
    private String duration;


    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
