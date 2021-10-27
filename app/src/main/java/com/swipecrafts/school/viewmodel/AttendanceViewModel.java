package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.api.AttendanceResponse;
import com.swipecrafts.school.data.model.db.SchoolClass;
import com.swipecrafts.school.data.model.db.SchoolSection;
import com.swipecrafts.school.data.repository.AttendanceRepository;
import com.swipecrafts.school.data.repository.ClassSectionRepository;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.school.ui.dashboard.attendance.model.StudentAttendance;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/30/2018.
 */
public class AttendanceViewModel extends ViewModel {
    private final RepositoryFactory repoFactory;
    private final AttendanceRepository attendanceRepository;
    private ClassSectionRepository classSectionRepository;

    public AttendanceViewModel(RepositoryFactory repoFactory) {
        this.repoFactory = repoFactory;
        this.classSectionRepository = repoFactory.create(ClassSectionRepository.class);
        this.attendanceRepository = repoFactory.create(AttendanceRepository.class);
    }

    public LiveData<Resource<List<SchoolClass>>> loadClasses() {
        return classSectionRepository.loadSchoolClasses();
    }

    public LiveData<List<SchoolSection>> getSections(long classId) {
        return classSectionRepository.getSectionsFromDB(classId);
    }

    public LiveData<ApiResponse<AttendanceResponse>> getAttendanceResponse(Long classId, Long sectionId) {
        return attendanceRepository.getAttendanceData(classId, sectionId);
    }

    public LiveData<ApiResponse<Boolean>> submitAttendance(List<StudentAttendance> studentAttendanceData) {
        return attendanceRepository.submitAttendance(studentAttendanceData);
    }

    public LiveData<ApiResponse<List<StudentAttendance>>> getMyAttendance() {
        return attendanceRepository.getMyAttendance();
    }

    @Deprecated
    public int getCalendarType() {
//        return dataManager.getPrefRepo().getSettings().getCalendarType();
        return 1;
    }
}
