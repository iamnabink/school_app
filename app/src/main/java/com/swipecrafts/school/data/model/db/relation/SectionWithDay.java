package com.swipecrafts.school.data.model.db.relation;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Relation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.swipecrafts.school.data.model.db.Day;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 5/23/2018.
 */
public class SectionWithDay {
    @SerializedName("section_id")
    @Expose
    @ColumnInfo(name = "section_id")
    private long sectionId;

    @SerializedName("section")
    @Expose
    @ColumnInfo(name = "section_name")
    private String section;

    @SerializedName("day")
    @Expose
    @Relation(parentColumn = "id", entityColumn = "section_id")
    private List<Day> day;

    public long getSectionId() {
        return sectionId;
    }

    public void setSectionId(long sectionId) {
        this.sectionId = sectionId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<Day> getDay() {
        return day;
    }

    public void setDay(List<Day> day) {
        this.day = day;
    }
}
