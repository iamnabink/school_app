package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Madhusudan Sapkota on 5/23/2018.
 */
@Entity(tableName = "days")
public class Day {

    @SerializedName("day_name")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "day_name")
    private String dayName;


    @NonNull
    public String getDayName() {
        return dayName;
    }

    public void setDayName(@NonNull String dayName) {
        this.dayName = dayName;
    }
}
