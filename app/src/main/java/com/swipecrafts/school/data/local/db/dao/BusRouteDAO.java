package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.swipecrafts.school.data.model.db.BusRoute;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/15/2018.
 */

@Dao
public abstract class BusRouteDAO implements BaseDAO<BusRoute>{
    @Query("SELECT COUNT(*) FROM bus_route")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM bus_route WHERE bus_id=:busId")
    public abstract LiveData<Integer> countByBus(long busId);

    @Query("SELECT COUNT(*) FROM bus_route WHERE bus_id=:busId")
    public abstract int countStaticByBus(long busId);

    @Query("SELECT * from bus_route")
    public abstract LiveData<List<BusRoute>> getAllBusRoute();

    @Query("SELECT * FROM bus_route WHERE bus_id=:busId")
    public abstract LiveData<List<BusRoute>> getBusRouteByBus(long busId);

    @Query("SELECT * FROM bus_route WHERE id = :id")
    public abstract LiveData<BusRoute> getBusRouteById(long id);

    @Query("DELETE FROM bus_route")
    public abstract void deleteAll();
}
