package com.swipecrafts.school.data.repository;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.remote.AppApiRepository;

/**
 * Created by Madhusudan Sapkota on 5/20/2018.
 */
public abstract class BaseRepository {
    public final AppApiRepository webService;
    public final AppDbRepository dbService;
    public final AppPreferencesRepository preferencesService;

    public BaseRepository(AppApiRepository webService, AppDbRepository dbService, AppPreferencesRepository preferencesRepository) {
        this.webService = webService;
        this.dbService = dbService;
        this.preferencesService = preferencesRepository;
    }
}
