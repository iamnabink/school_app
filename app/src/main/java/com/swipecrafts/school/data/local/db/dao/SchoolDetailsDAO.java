package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.swipecrafts.school.data.model.db.SchoolDetails;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 4/3/2018.
 */
@Dao
public abstract class SchoolDetailsDAO implements BaseDAO<SchoolDetails> {
    @Query("SELECT COUNT(*) FROM school_profile")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM school_profile")
    public abstract int countStatic();

    @Query("SELECT * FROM school_profile WHERE id=:schoolId")
    public abstract LiveData<SchoolDetails> getSchoolById(long schoolId);

    @Query("SELECT * from school_profile")
    public abstract LiveData<List<SchoolDetails>> getAllSchools();

    @Query("DELETE FROM school_profile")
    public abstract void deleteAll();

    @Transaction
    public Long deleteAndInsert(SchoolDetails schoolDetails){
       if (schoolDetails == null) return  0L;
        deleteAll();
        return insert(schoolDetails);
    }
}
