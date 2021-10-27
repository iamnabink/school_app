package com.swipecrafts.school.ui.dashboard.examroutine.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 4/6/2018.
 */
public class ExamRoutineRes {

    @SerializedName("examid")
    @Expose
    private long id;

    @SerializedName("exam")
    @Expose
    private String examType;

    @SerializedName("year")
    @Expose
    private String year;

    @SerializedName("exam_routine")
    @Expose
    private List<ExamRoutine> routine;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<ExamRoutine> getRoutine() {
        return routine;
    }

    public void setRoutine(List<ExamRoutine> routine) {
        this.routine = routine;
    }
}
