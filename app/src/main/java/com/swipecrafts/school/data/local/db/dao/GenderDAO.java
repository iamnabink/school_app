package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.swipecrafts.school.data.model.db.Gender;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/16/2018.
 */

@Dao
public abstract class GenderDAO implements BaseDAO<Gender>{

    @Query("SELECT COUNT(*) FROM gender")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM gender")
    public abstract int countStatic();

    @Query("SELECT COUNT(*) FROM gender WHERE day_name=:dayName")
    public abstract LiveData<Integer> countByDay(String dayName);

    @Query("SELECT COUNT(*) FROM gender WHERE day_name=:dayName")
    public abstract int countStaticByDay(String dayName);

    @Query("SELECT * from gender")
    public abstract LiveData<List<Gender>> getAllGender();

    @Query("SELECT * from gender")
    public abstract List<Gender> getStaticGenderList();

    @Query("SELECT * FROM gender WHERE id = :id")
    public abstract LiveData<Gender> getGenderById(long id);

    @Query("DELETE FROM gender")
    public abstract void deleteAll();
}
