package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.swipecrafts.library.spinner.SpinnerModel;

/**
 * Created by Madhusudan Sapkota on 4/3/2018.
 */
@Entity(tableName = "classes")
public class SchoolClass implements SpinnerModel {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    private long classId;

    @ColumnInfo(name = "class_name")
    private String className;

    public SchoolClass() {
    }

    @Ignore
    public SchoolClass(@NonNull long classId, String className) {
        this.classId = classId;
        this.className = className;
    }

    @NonNull
    public long getClassId() {
        return classId;
    }

    public void setClassId(@NonNull long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
