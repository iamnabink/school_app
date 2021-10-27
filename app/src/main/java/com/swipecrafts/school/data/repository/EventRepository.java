package com.swipecrafts.school.data.repository;

import android.arch.lifecycle.LiveData;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.model.db.Event;
import com.swipecrafts.school.data.remote.AppApiRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 5/31/2018.
 */
public class EventRepository extends BaseRepository {

    public EventRepository(AppApiRepository webService, AppDbRepository dbService, AppPreferencesRepository preferencesRepository) {
        super(webService, dbService, preferencesRepository);
    }

    public LiveData<List<Event>> loadEvents(Date from, Date to){
        return dbService.getEventDAO().getAllEvents(from, to);
    }

    public LiveData<List<Date>> loadAllEventsDate() {
        return dbService.getEventDAO().getAllEventsDate();
    }
}
