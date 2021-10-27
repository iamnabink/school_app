
package com.swipecrafts.school.data.model.db;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class DressCode {

    @SerializedName("day")
    @Expose
    private String mDay;

    @SerializedName("gender")
    @Expose
    private List<Gender> mGender;

    public DressCode(String mDay, List<Gender> mGender) {
        this.mDay = mDay;
        this.mGender = mGender;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        mDay = day;
    }

    public List<Gender> getGender() {
        return mGender;
    }

    public void setGender(List<Gender> gender) {
        mGender = gender;
    }

    public void addGender(Gender gender){
        if (mGender.size() == 2) return;
        mGender.add(gender);
    }

    public boolean checkIfPresent(Gender gender){
        for (Gender g: mGender){
            if (g.getCategoryId() == gender.getCategoryId()) return true;
        }
        return false;
    }

}
