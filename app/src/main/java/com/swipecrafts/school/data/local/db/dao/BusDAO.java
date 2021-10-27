package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.swipecrafts.school.data.model.db.Bus;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/15/2018.
 */

@Dao
public abstract class BusDAO implements BaseDAO<Bus> {

    @Query("SELECT COUNT(*) FROM bus")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM bus")
    public abstract int countStatic();

    @Query("SELECT * from bus")
    public abstract LiveData<List<Bus>> getAllBus();

    @Query("SELECT * FROM bus WHERE id = :id")
    public abstract LiveData<Bus> getBusById(long id);

    @Query("DELETE FROM bus")
    public abstract void deleteAll();
}
