package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.swipecrafts.school.data.model.db.SchoolClass;
import com.swipecrafts.school.data.model.db.SchoolSection;
import com.swipecrafts.school.data.model.db.relation.ClassSection;
import com.swipecrafts.school.data.model.db.relation.ClassWithSections;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Madhusudan Sapkota on 5/23/2018.
 */
@Dao
public abstract class ClassSectionDAO {

    @Query("SELECT * FROM classes")
    public abstract LiveData<List<SchoolClass>> getClasses();

    @Query("SELECT * from classes")
    public abstract LiveData<List<ClassWithSections>> getClassWithSection();

    @Query("SELECT * FROM sections WHERE class_id=:classId")
    public abstract LiveData<List<SchoolSection>> getSectionsByClass(long classId);

    @Query("SELECT * FROM sections INNER JOIN class_section ON sections.id = class_section.section_id WHERE class_section.class_id = :classId")
    public abstract LiveData<List<SchoolSection>> getSectionsByClassId(long classId);

    @Transaction
    public void insertClassAndSection(List<ClassWithSections> items) {
        if (items == null || items.isEmpty()) return;

        deleteAllClasses();
        deleteAllSections();
        for (ClassWithSections cs : items) {
            insertClasses(cs.getSchoolClass());
            insertSections(cs.getSchoolSections());
        }
    }

    @Transaction
    public void insertClassAndSection(List<SchoolClass> classes, List<SchoolSection> sections, List<ClassSection> classSections) {

        if (classes != null && !classes.isEmpty()) {
            deleteAllClasses();
            deleteAllSections();
            insertClasses(classes);
        }

        if (sections != null && !sections.isEmpty()) {
            deleteAllSections();
            insertSections(sections);
        }

        deleteAllClassSectionRelation();
        if (classSections != null && !classSections.isEmpty()){
            insertClassSections(classSections);
        }
    }

    @Query("DELETE FROM class_section")
    protected abstract void deleteAllClassSectionRelation() ;

    @Insert(onConflict = REPLACE)
    protected abstract void insertClasses(List<SchoolClass> items);

    @Insert(onConflict = REPLACE)
    protected abstract void insertClassSections(List<ClassSection> items);

    @Insert(onConflict = REPLACE)
    protected abstract void insertClasses(SchoolClass item);

    @Insert(onConflict = REPLACE)
    protected abstract void insertSections(List<SchoolSection> items);

    @Insert(onConflict = REPLACE)
    protected abstract void insertSections(SchoolSection item);

    @Query("DELETE FROM classes")
    public abstract void deleteAllClasses();

    @Query("DELETE FROM sections")
    public abstract void deleteAllSections();
}
