package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.swipecrafts.school.data.model.db.SchoolSchedule;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/14/2018.
 */

@Dao
public abstract class SchoolScheduleDAO implements BaseDAO<SchoolSchedule> {

    @Query("SELECT COUNT(*) FROM school_schedule")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM school_schedule")
    public abstract int countStatic();

    @Query("SELECT * from school_schedule")
    public abstract LiveData<List<SchoolSchedule>> getAllSchoolSchedule();

    @Query("SELECT * from school_schedule WHERE day_name =:day")
    public abstract LiveData<List<SchoolSchedule>> getSchoolScheduleByDay(String day);

    @Query("DELETE FROM school_schedule")
    public abstract void deleteAll();
}
