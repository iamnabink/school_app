package com.swipecrafts.school.ui.splash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Madhusudan Sapkota on 4/6/2018.
 */
public class SchoolId {
    @SerializedName("school_id")
    @Expose
    private String schoolId;

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
}
