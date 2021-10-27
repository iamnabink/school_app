package com.swipecrafts.school.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.manager.NetworkBoundResource;
import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.db.SchoolClass;
import com.swipecrafts.school.data.model.db.SchoolSection;
import com.swipecrafts.school.data.model.db.relation.ClassSection;
import com.swipecrafts.school.data.remote.AppApiRepository;
import com.swipecrafts.school.ui.dashboard.attendance.model.StudentAttendance;
import com.swipecrafts.school.ui.dashboard.schedule.model.ClassResponse;
import com.swipecrafts.school.ui.dashboard.schedule.model.Section;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 5/23/2018.
 */
public class ClassSectionRepository extends BaseRepository {

    public ClassSectionRepository(AppApiRepository webService, AppDbRepository dbService, AppPreferencesRepository preferencesRepository) {
        super(webService, dbService, preferencesRepository);
    }

    public LiveData<Resource<List<SchoolClass>>> loadSchoolClasses() {
        return new NetworkBoundResource<List<SchoolClass>, List<ClassResponse>>() {
            @Override
            protected void saveCallResult(@NonNull List<ClassResponse> item) {
                List<SchoolClass> classes = new ArrayList<>();
                List<SchoolSection> sections = new ArrayList<>();
                List<ClassSection> classSections = new ArrayList<>();

                for (ClassResponse sc : item) {
                    classes.add(new SchoolClass(sc.getClassId(), sc.getClassName()));
                    for (Section s : sc.getSection()) {
                        sections.add(new SchoolSection(s.getSectionId(), s.getSection(), sc.getClassId()));
                        classSections.add(new ClassSection(sc.getClassId(), s.getId()));
                    }
                }
                dbService.getClassSectionDAO().insertClassAndSection(classes, sections, classSections);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SchoolClass> data) {
                return (data == null || data.isEmpty());
            }

            @NonNull
            @Override
            protected LiveData<List<SchoolClass>> loadFromDb() {
                return dbService.getClassSectionDAO().getClasses();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ClassResponse>>> createCall() {
                return webService.getClassesAndSections();
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<SchoolSection>>> loadSchoolSections(long classId) {
        return new NetworkBoundResource<List<SchoolSection>, List<ClassResponse>>() {
            @Override
            protected void saveCallResult(@NonNull List<ClassResponse> item) {
                List<SchoolClass> classes = new ArrayList<>();
                List<SchoolSection> sections = new ArrayList<>();
                List<ClassSection> classSections = new ArrayList<>();

                for (ClassResponse sc : item) {
                    classes.add(new SchoolClass(sc.getClassId(), sc.getClassName()));
                    for (Section s : sc.getSection()) {
                        sections.add(new SchoolSection(s.getSectionId(), s.getSection(), sc.getClassId()));
                        classSections.add(new ClassSection(sc.getClassId(), s.getId()));
                    }
                }
                dbService.getClassSectionDAO().insertClassAndSection(classes, sections, classSections);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SchoolSection> data) {
                Log.e("DB Data check", "Is Null " + (data == null) + " is Empty " + (data != null ? data.isEmpty() : ""));
                return (data == null || data.isEmpty());
            }

            @NonNull
            @Override
            protected LiveData<List<SchoolSection>> loadFromDb() {
                return dbService.getClassSectionDAO().getSectionsByClassId(classId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ClassResponse>>> createCall() {
                return webService.getClassesAndSections();
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<StudentAttendance>>> loadStudents(Long classId, Long sectionId) {
        MediatorLiveData<Resource<List<StudentAttendance>>> result = new MediatorLiveData<>();
        LiveData<ApiResponse<List<StudentAttendance>>> apiData = webService.getStudentsByClassAndOrSection(classId, sectionId);
        result.addSource(apiData, newData ->{
            if (newData == null) return;
            if (newData.isLoading()) return;

            if (newData.isSuccessful()){
                result.setValue(Resource.success(newData.data));
            }else {
                result.setValue(Resource.error(newData.status, newData.message, newData.data));
            }
        });
        return result;
    }

    public LiveData<List<SchoolSection>> getSectionsFromDB(long classId) {
        return dbService.getClassSectionDAO().getSectionsByClassId(classId);
    }
}
