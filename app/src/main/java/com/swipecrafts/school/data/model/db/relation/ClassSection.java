package com.swipecrafts.school.data.model.db.relation;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import com.swipecrafts.school.data.model.db.SchoolClass;
import com.swipecrafts.school.data.model.db.SchoolSection;

/**
 * Created by Madhusudan Sapkota on 6/23/2019.
 */
//primaryKeys = ["reservationId", "guestId"],
//        foreignKeys = [
//            ForeignKey(entity = Reservation::class,
//                    parentColumns = ["id"],
//                    childColumns = ["reservationId"]),
//            ForeignKey(entity = Guest::class,
//                    parentColumns = ["id"],
//                    childColumns = ["guestId"])
//        ]
@Entity(
        primaryKeys = {"class_id", "section_id"},
        foreignKeys = {
                @ForeignKey(entity = SchoolClass.class, parentColumns = "id", childColumns = "class_id"),
                @ForeignKey(entity = SchoolSection.class, parentColumns = "id", childColumns = "section_id")
        },
        tableName = "class_section"
)
public class ClassSection {

    @ColumnInfo(name = "class_id")
    private long classId;

    @ColumnInfo(name = "section_id")
    private long sectionId;

    public ClassSection() {
    }

    public ClassSection(long classId, long sectionId) {
        this.classId = classId;
        this.sectionId = sectionId;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public long getSectionId() {
        return sectionId;
    }

    public void setSectionId(long sectionId) {
        this.sectionId = sectionId;
    }
}
