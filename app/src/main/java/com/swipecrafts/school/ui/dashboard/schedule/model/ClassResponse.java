package com.swipecrafts.school.ui.dashboard.schedule.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.swipecrafts.library.spinner.SpinnerModel;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/25/2018.
 */

public class ClassResponse implements SpinnerModel{

    @SerializedName("class_id")
    @Expose
    private long classId;

    @SerializedName("class_name")
    @Expose
    private String className;

    @SerializedName("section")
    @Expose
    private List<Section> section = null;

    public ClassResponse() {
    }

    public ClassResponse(long classId, String className) {
        this.classId = classId;
        this.className = className;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Section> getSection() {
        return section;
    }

    public void setSection(List<Section> section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return "{\n" +
                "        \"class_id\": \""+ classId +"\",\n" +
                "        \"class_name\": \""+ className +"\",\n" +
                "        \"section\": \n" + (section == null ? "null" : section.toString()) +" \n" +
                "    }";
    }

    @Override
    public int getId() {
        return (int) classId;
    }

    @Override
    public String getDisplayText() {
        return className;
    }
}
