package com.swipecrafts.school.data.repository;

import android.arch.lifecycle.LiveData;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.remote.AppApiRepository;
import com.swipecrafts.school.ui.dashboard.assignment.model.Assignment;

import java.io.File;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 6/6/2018.
 */
public class AssignmentRepository extends BaseRepository {

    public AssignmentRepository(AppApiRepository webService, AppDbRepository dbService, AppPreferencesRepository preferencesRepository) {
        super(webService, dbService, preferencesRepository);
    }

    public LiveData<ApiResponse<String>> postAssignment(String title, int classId, int subjectId, File selectedFile) {
        return webService.postAssignment(title, classId, subjectId, selectedFile);
    }

    public LiveData<ApiResponse<List<Assignment>>> getAssignments(){
        return webService.getAssignmentList();
    }
}
