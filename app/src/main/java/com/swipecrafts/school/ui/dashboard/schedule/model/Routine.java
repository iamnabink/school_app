package com.swipecrafts.school.ui.dashboard.schedule.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Madhusudan Sapkota on 3/25/2018.
 */

public class Routine {

    @SerializedName("period")
    @Expose
    private String period;
    @SerializedName("teacher")
    @Expose
    private String teacher;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("subject")
    @Expose
    private String subject;

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

    @Override
    public String toString() {
        return "{\n" +
                "                                \"period\": "+ period +",\n" +
                "                                \"teacher\": "+ teacher +",\n" +
                "                                \"start_time\": "+ startTime +",\n" +
                "                                \"end_time\": " + endTime + ",\n" +
                "                                \"subject\": "+ subject +"\n" +
                "                            }";
    }
}
