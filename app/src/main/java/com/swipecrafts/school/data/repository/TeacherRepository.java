package com.swipecrafts.school.data.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.manager.NetworkBoundResource;
import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.db.Teacher;
import com.swipecrafts.school.data.remote.AppApiRepository;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 5/22/2018.
 */
public class TeacherRepository extends BaseRepository {

    public TeacherRepository(AppApiRepository webService, AppDbRepository dbService, AppPreferencesRepository preferencesRepository) {
        super(webService, dbService, preferencesRepository);
    }

    public LiveData<Resource<List<Teacher>>> loadTeachers(boolean fetchFromRemote){
        return new NetworkBoundResource<List<Teacher>, List<Teacher>>() {
            @Override
            protected void saveCallResult(@NonNull List<Teacher> item) {
                dbService.getTeacherDAO().deleteAndInsert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Teacher> data) {
                return (fetchFromRemote || data == null || data.isEmpty());
            }

            @NonNull
            @Override
            protected LiveData<List<Teacher>> loadFromDb() {
                return dbService.getTeacherDAO().getTeachers();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Teacher>>> createCall() {
                return webService.getTeacherContactList();
            }
        }.getAsLiveData();
    }
}
