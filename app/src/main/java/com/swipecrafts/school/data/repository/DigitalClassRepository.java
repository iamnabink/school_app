package com.swipecrafts.school.data.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.manager.NetworkBoundResource;
import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.db.Grade;
import com.swipecrafts.school.data.remote.AppApiRepository;
import com.swipecrafts.school.ui.dc.Video.VideoFeed;
import com.swipecrafts.school.ui.dc.dcfeed.ContentFeed;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 5/21/2018.
 */
public class DigitalClassRepository extends BaseRepository {

    public DigitalClassRepository(AppApiRepository webService, AppDbRepository dbService, AppPreferencesRepository preferencesRepository) {
        super(webService, dbService, preferencesRepository);
    }

    public LiveData<Resource<List<Grade>>> loadGrades(boolean fetchFromRemote){
        return new NetworkBoundResource<List<Grade>, List<Grade>>() {
            @Override
            protected void saveCallResult(@NonNull List<Grade> item) {
                dbService.getGradeDAO().deleteAndInsert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Grade> data) {
                return (fetchFromRemote || data == null || data.isEmpty());
            }

            @NonNull
            @Override
            protected LiveData<List<Grade>> loadFromDb() {
                return dbService.getGradeDAO().getAllGrades();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Grade>>> createCall() {
                return webService.getGradeList();
            }
        }.getAsLiveData();
    }

    public LiveData<ApiResponse<List<ContentFeed>>> getDigitalContentFeed(long gradeId, long subjectId, long chapterId) {
        return webService.getContentFeeds(gradeId, subjectId, chapterId);
    }


    public LiveData<ApiResponse<List<VideoFeed>>> getVideoContentFeed(long gradeId, long subjectId) {
        return webService.getVideoFeeds(gradeId,subjectId);
    }
}
