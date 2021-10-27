package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.swipecrafts.school.data.model.db.DCSubject;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/5/2018.
 */

@Dao
public abstract class SubjectDAO implements BaseDAO<DCSubject> {

    @Query("SELECT COUNT(*) FROM subjects")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM subjects")
    public abstract int countStatic();

    @Query("SELECT COUNT(*) FROM subjects WHERE gradeId=:gradeId")
    public abstract LiveData<Integer> countByGrade(long gradeId);

    @Query("SELECT COUNT(*) FROM subjects WHERE gradeId=:gradeId")
    public abstract int countStaticByGrade(long gradeId);

    @Query("SELECT * FROM subjects")
    public abstract LiveData<List<DCSubject>> getAllSubjects();

    @Query("SELECT * FROM subjects WHERE gradeId=:gradeId")
    public abstract LiveData<List<DCSubject>> getSubjectsByGrade(long gradeId);

    @Query("SELECT * FROM subjects WHERE id = :id")
    public abstract LiveData<DCSubject> getSubjectById(long id);

    @Query("DELETE FROM subjects")
    public abstract void deleteAll();
}
