package com.swipecrafts.school.ui.dashboard.schedule.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/25/2018.
 */

public class Day {

    @SerializedName("day_name")
    @Expose
    private String dayName;
    @SerializedName("routine")
    @Expose
    private List<Routine> routine = null;

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public List<Routine> getRoutine() {
        return routine;
    }

    public void setRoutine(List<Routine> routine) {
        this.routine = routine;
    }

    @Override
    public String toString() {
        return " {\n" +
                "                        \"day_name\": \"Sunday\",\n" +
                "                        \"routine\": [\n" + routine.toString() +"]\n" +
                "                    }";
    }
}
