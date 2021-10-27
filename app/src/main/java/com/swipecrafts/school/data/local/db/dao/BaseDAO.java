package com.swipecrafts.school.data.local.db.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 2/20/2018.
 */

public interface BaseDAO<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<T> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(T... args);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(T item);

    @Update
    void update(T item);

    @Update
    public void update(List<T> items);

    @Delete
    void delete(T item);
}
