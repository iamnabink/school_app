package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.swipecrafts.school.data.model.db.BusDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 11/15/2018.
 */
@Dao
public abstract class BusDriverDAO implements BaseDAO<BusDriver> {

    @Query("SELECT COUNT(*) FROM bus_drivers")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM bus_drivers")
    public abstract int countStatic();

    @Query("SELECT * from bus_drivers")
    public abstract LiveData<List<BusDriver>> getAllBusAnnDriver();

    @Query("SELECT * FROM bus_drivers WHERE id = :id")
    public abstract LiveData<BusDriver> getBusDriverById(long id);

    @Query("SELECT * FROM bus_drivers WHERE driver_id=:id")
    public abstract LiveData<List<BusDriver>> getBusDriverByDriverId(long id);

    public List<Long> deleteAndInsert(List<BusDriver> data){
        if (data ==null || data.isEmpty()) return new ArrayList<>();

        deleteAll();
        return insertAll(data);
    }

    @Query("DELETE FROM bus_drivers")
    public abstract void deleteAll();
}