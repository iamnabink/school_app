package com.swipecrafts.school.data.model.db.relation;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Relation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.swipecrafts.school.ui.dashboard.schedule.model.Routine;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 5/23/2018.
 */
public class DayWithRoutine {

    @SerializedName("day_name")
    @Expose
    @ColumnInfo(name = "day_name")
    private String dayName;

    @SerializedName("routine")
    @Expose
    @Relation(parentColumn = "id", entityColumn = "day_name")
    private List<Routine> routine;

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
}
