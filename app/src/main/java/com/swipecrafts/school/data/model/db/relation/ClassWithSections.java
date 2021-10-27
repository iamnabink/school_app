package com.swipecrafts.school.data.model.db.relation;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.swipecrafts.school.data.model.db.SchoolClass;
import com.swipecrafts.school.data.model.db.SchoolSection;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 5/23/2018.
 */
public class ClassWithSections {

    @SerializedName("section")
    @Expose
    @Relation(parentColumn = "id", entityColumn = "class_id")
    private List<SchoolSection> schoolSections;

    @Embedded
    private SchoolClass schoolClass;

    public List<SchoolSection> getSchoolSections() {
        return schoolSections;
    }

    public void setSchoolSections(List<SchoolSection> schoolSections) {
        this.schoolSections = schoolSections;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }
}
