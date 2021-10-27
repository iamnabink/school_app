package com.swipecrafts.school.ui.dashboard.schedule.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.swipecrafts.library.spinner.SpinnerModel;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/25/2018.
 */

public class Section implements SpinnerModel {

    @SerializedName("section_id")
    @Expose
    private long sectionId;

    @SerializedName("section")
    @Expose
    private String section;

    @SerializedName("day")
    @Expose
    private List<Day> day = null;

    public Section() {
    }

    public Section(long sectionId, String section) {
        this.sectionId = sectionId;
        this.section = section;
    }

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

    @Override
    public String toString() {
        return "{\n" +
                "                \"section\": \"" + section + "\",\n" +
                "                \"day\": \n\"" + (day == null ? "null" : day.toString()) + "\n" +
                "            }";
    }

    @Override
    public int getId() {
        return (int) sectionId;
    }

    @Override
    public String getDisplayText() {
        return section;
    }
}
