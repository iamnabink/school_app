package com.swipecrafts.school.data.repository;

import android.arch.lifecycle.LiveData;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.api.AttendanceResponse;
import com.swipecrafts.school.data.remote.AppApiRepository;
import com.swipecrafts.school.ui.dashboard.attendance.model.StudentAttendance;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 5/28/2018.
 */
public class AttendanceRepository extends BaseRepository {

    public AttendanceRepository(AppApiRepository webService, AppDbRepository dbService, AppPreferencesRepository preferencesRepository) {
        super(webService, dbService, preferencesRepository);
    }

    public LiveData<ApiResponse<AttendanceResponse>> getAttendanceData(Long classId, Long sectionId){
        return webService.getStudentAttendance(classId, sectionId);
    }

    public LiveData<ApiResponse<Boolean>> submitAttendance(List<StudentAttendance> studentAttendanceData) {
        return webService.submitAttendance(studentAttendanceData);
    }

    public LiveData<ApiResponse<List<StudentAttendance>>> getMyAttendance() {
        return webService.getMyAttendance();
    }
}
