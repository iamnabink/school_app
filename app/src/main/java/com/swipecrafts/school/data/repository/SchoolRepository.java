package com.swipecrafts.school.data.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.manager.NetworkBoundResource;
import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.db.SchoolDetails;
import com.swipecrafts.school.data.remote.AppApiRepository;

/**
 * Created by Madhusudan Sapkota on 6/4/2018.
 */
public class SchoolRepository extends BaseRepository {

    public SchoolRepository(AppApiRepository webService, AppDbRepository dbService, AppPreferencesRepository preferencesRepository) {
        super(webService, dbService, preferencesRepository);
    }

    public LiveData<Resource<SchoolDetails>> loadSchoolDetails(boolean fetchFromNetwork){
        return new NetworkBoundResource<SchoolDetails, SchoolDetails>() {
            @Override
            protected void saveCallResult(@NonNull SchoolDetails item) {
                dbService.getSchoolDetailsDAO().deleteAndInsert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable SchoolDetails data) {
                return (data == null) || fetchFromNetwork;
            }

            @NonNull
            @Override
            protected LiveData<SchoolDetails> loadFromDb() {
                return dbService.getSchoolDetailsDAO().getSchoolById(Long.parseLong(preferencesService.getSchoolId()));
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<SchoolDetails>> createCall() {
                return webService.getSchoolDetails();
            }
        }.getAsLiveData();
    }
}
