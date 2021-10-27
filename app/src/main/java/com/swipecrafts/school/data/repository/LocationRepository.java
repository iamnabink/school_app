package com.swipecrafts.school.data.repository;

import android.location.Location;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.remote.AppApiRepository;

/**
 * Created by Madhusudan Sapkota on 7/23/2018.
 */
public class LocationRepository extends BaseRepository{

    public LocationRepository(AppApiRepository webService, AppDbRepository dbService, AppPreferencesRepository preferencesRepository) {
        super(webService, dbService, preferencesRepository);
    }

    public void postLocation(Location location) {
        webService.broadCaseLocation(location);
    }

    public long getUserId() {
        return preferencesService.getProtectedApiHeader().getSESUId();
    }
}
