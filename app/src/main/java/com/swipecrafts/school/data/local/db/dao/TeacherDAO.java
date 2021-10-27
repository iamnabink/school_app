package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.swipecrafts.school.data.model.db.Teacher;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 5/22/2018.
 */
@Dao
public abstract class TeacherDAO implements BaseDAO<Teacher> {

    @Query("SELECT * FROM teachers")
    public abstract LiveData<List<Teacher>> getTeachers();

    @Query("DELETE FROM teachers")
    public abstract void deleteAll();

    @Transaction
    public List<Long> deleteAndInsert(List<Teacher> item){
        deleteAll();
        return insertAll(item);
    }
}
