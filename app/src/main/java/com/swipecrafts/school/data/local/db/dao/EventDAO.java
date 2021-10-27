package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.swipecrafts.school.data.model.db.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 2/27/2018.
 */

@Dao
public abstract class EventDAO implements BaseDAO<Event>{

    @Query("SELECT COUNT(*) FROM events")
    public abstract LiveData<Integer> count();

    @Query("SELECT COUNT(*) FROM events")
    public abstract int countStatic();

    @Query("SELECT * from events ORDER By event_start_date ASC")
    public abstract LiveData<List<Event>> getAllEvents();

    @Query("SELECT * from events ORDER By event_start_date DESC")
    public abstract LiveData<List<Event>> getAllEventsDESC();

    @Query("SELECT * from events WHERE event_start_date BETWEEN :from AND :to ORDER By event_start_date ASC")
    public abstract LiveData<List<Event>> getAllEvents(Date from, Date to);

    @Query("SELECT event_start_date from events ORDER By event_start_date ASC")
    public abstract LiveData<List<Date>> getAllEventsDate();

    @Query("SELECT * from events WHERE event_category LIKE:category ORDER By event_start_date ASC")
    public abstract LiveData<List<Event>> getEventsByCategory(String category);

    @Query("SELECT * FROM events WHERE id = :id")
    public abstract LiveData<Event> getEventsById(String id);

    @Transaction
    public List<Long> deleteAndInsert(List<Event> events){
        if (events == null || events.isEmpty()) return new ArrayList<>();
        deleteAll();
        return insertAll(events);
    }

    @Query("SELECT * FROM events ORDER BY id DESC LIMIT 1")
    public abstract LiveData<Event> getLastItem();

    @Query("DELETE FROM events")
    public abstract void deleteAll();

}
