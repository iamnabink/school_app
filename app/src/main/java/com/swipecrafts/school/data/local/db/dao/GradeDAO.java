package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.swipecrafts.school.data.model.db.Grade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/4/2018.
 */

@Dao
public abstract class GradeDAO implements BaseDAO<Grade>{

    @Query("SELECT COUNT(*) FROM grades")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM grades")
    public abstract int countStatic();

    @Query("SELECT * from grades ORDER By grade_order")
    public abstract LiveData<List<Grade>> getAllGrades();

    @Query("SELECT * FROM grades WHERE id = :id")
    public abstract LiveData<Grade> getGradesById(String id);

    @Transaction
    public List<Long> deleteAndInsert(List<Grade> items) {
        if (items == null || items.isEmpty()) return new ArrayList<>();
        deleteAll();
        return insertAll(items);
    }

    @Query("DELETE FROM grades")
    public abstract void deleteAll();
}
