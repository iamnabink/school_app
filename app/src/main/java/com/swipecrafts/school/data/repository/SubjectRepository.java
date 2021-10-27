package com.swipecrafts.school.data.repository;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.remote.AppApiRepository;
import com.swipecrafts.school.ui.dashboard.assignment.model.Subject;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 6/6/2018.
 */
public class SubjectRepository extends BaseRepository {

    public SubjectRepository(AppApiRepository webService, AppDbRepository dbService, AppPreferencesRepository preferencesRepository) {
        super(webService, dbService, preferencesRepository);
    }

    public LiveData<ApiResponse<List<Subject>>> loadSubjects() {
        return webService.getSchoolSubjectList();
    }
}
