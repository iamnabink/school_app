package com.swipecrafts.school.ui.dashboard.assignment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.swipecrafts.library.spinner.SpinnerModel;

/**
 * Created by Madhusudan Sapkota on 4/5/2018.
 */
public class Subject implements SpinnerModel{

    @SerializedName("subid")
    @Expose
    private long subjectId;

    @SerializedName("subject")
    @Expose
    private String subjectName;

    public Subject(long subjectId, String subjectName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public int getId() {
        return (int) subjectId;
    }

    @Override
    public String getDisplayText() {
        return subjectName;
    }
}
