package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.swipecrafts.library.spinner.SpinnerModel;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Madhusudan Sapkota on 4/3/2018.
 */
@Entity(tableName = "sections", foreignKeys = @ForeignKey(entity = SchoolClass.class,
        parentColumns = "id",
        childColumns = "class_id",
        onDelete = CASCADE))
public class SchoolSection implements SpinnerModel {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private long sectionId;

    @ColumnInfo(name = "section_name")
    private String section;

    @ColumnInfo(name = "class_id")
    private long classId;

    @NonNull
    public long getSectionId() {
        return sectionId;
    }

    public SchoolSection() {
    }

    @Ignore
    public SchoolSection(@NonNull long sectionId, String section, long classId) {
        this.sectionId = sectionId;
        this.section = section;
        this.classId = classId;
    }

    @Ignore
    public SchoolSection(@NonNull long sectionId, String section) {
        this.sectionId = sectionId;
        this.section = section;
    }

    public void setSectionId(@NonNull long sectionId) {
        this.sectionId = sectionId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    @Override
    public int getId() {
        return (int) sectionId;
    }

    @Override
    public String getDisplayText() {
        return section;
    }

    @Override
    public String toString() {
        return "{\n" +
                "                \"class id\": \"" + classId + "\",\n" +
                "                \"section id\": \"" + sectionId + "\",\n" +
                "                \"section\": \"" + section + "\" \n" +
                "            }";
    }
}
