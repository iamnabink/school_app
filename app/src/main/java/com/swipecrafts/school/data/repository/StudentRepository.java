package com.swipecrafts.school.data.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.manager.NetworkBoundResource;
import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.db.Parent;
import com.swipecrafts.school.data.model.db.Student;
import com.swipecrafts.school.data.model.db.relation.UserParent;
import com.swipecrafts.school.data.remote.AppApiRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 6/4/2018.
 */
public class StudentRepository extends BaseRepository {

    public StudentRepository(AppApiRepository webService, AppDbRepository dbService, AppPreferencesRepository preferencesRepository) {
        super(webService, dbService, preferencesRepository);
    }


    public LiveData<Resource<Student>> loadStudents(boolean refresh) {
        return new NetworkBoundResource<Student, Student>() {
            @Override
            protected void saveCallResult(@NonNull Student item) {
                Log.e("Students", "Saving students data");
                dbService.getStudentDAO().deleteAndInsert(item);
                dbService.getParentDAO().deleteAndInsert(item.getParents());
                List<UserParent> userParents = new ArrayList<>();
                for (Parent p : item.getParents()) userParents.add(new UserParent(item.getStudentId(), p.getParentId()));
                dbService.getUserParentDAO().insertAll(userParents);
            }

            @Override
            protected boolean shouldFetch(@Nullable Student data) {
                Log.e("Fetch", refresh +" : "+ (data == null) +" both "+ (refresh || (data == null)));
                return (refresh || data == null);
            }

            @NonNull
            @Override
            protected LiveData<Student> loadFromDb() {
                return dbService.getStudentDAO().getActiveStudentDetails();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Student>> createCall() {
                Log.e("Student", "Creating call Student data!!");
                return webService.getStudentDetails();
            }
        }.getAsLiveData();
    }

    public LiveData<List<Parent>> loadParents(long studentId){
        return dbService.getUserParentDAO().getParents(studentId);
    }

}
