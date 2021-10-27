package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.db.Parent;
import com.swipecrafts.school.data.model.db.Student;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.school.data.repository.StudentRepository;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/21/2018.
 */

public class StudentViewModel extends ViewModel{

    private RepositoryFactory dataManager;
    private final StudentRepository studentRepository;

    public StudentViewModel(RepositoryFactory dataManager) {
        this.dataManager = dataManager;
        this.studentRepository = dataManager.create(StudentRepository.class);
    }

    public LiveData<Resource<Student>> loadStudents(boolean refresh){
        return studentRepository.loadStudents(refresh);
    }

    public LiveData<List<Parent>> loadParents(long studentId){
        return studentRepository.loadParents(studentId);
    }

//    public LiveLocationData<Pair<String, Boolean>> refreshStudentDetails(){
//        MutableLiveData<Pair<String, Boolean>> data = new MutableLiveData<>();
//
//        dataManager.getApiRepo().getStudentDetails(new ApiListener<Student>() {
//            @Override
//            public void onSuccess(Student response) {
//                response.setUserId(dataManager.getUserId());
//                new AsyncTask<Student, Void, Void>() {
//                    @Override
//                    protected Void doInBackground(Student... args) {
//                        long stdId = dataManager.getDbRepo().getStudentDAO().insert(args[0]);
//                        Log.e("StudentID", stdId+"");
//
//                        List<Long> parentsID = dataManager.getDbRepo().getParentDAO().insertAll(args[0].getParents());
//                        Log.e("ParentsId", Arrays.toString(parentsID.toArray()));
//
//                        List<UserParent> userParents = new ArrayList<>();
//
//                        for (Parent p : args[0].getParents())
//                            userParents.add(new UserParent(dataManager.getUserId(), p.getParentId()));
//
//                        List<Long> userParentsID = dataManager.getDbRepo().getUserParentDAO().insertAll(userParents);
//                        Log.e("UserParentsId", Arrays.toString(userParentsID.toArray()));
//
//                        data.postValue(new Pair<>("Success!!", true));
//                        return null;
//                    }
//                }.execute(response);
//            }
//
//            @Override
//            public void onFailed(String message, int code) {
//                message = (code == 1001 ? dataManager.isNetworkConntected() ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.No_INTERNET_MESSAGE : message);
//
//                data.postValue(new Pair<>(message, false));
//            }
//        });
//
//        return data;
//    }
//
//    public LiveLocationData<Student> getActiveStudentDetails() {
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                for (Student s :
//                        dataManager.getDbRepo().getStudentDAO().getStaticStudentList()) {
//                    Log.e("insertedStudent", s.getName()+" userID "+ s.getUserId());
//                }
//
//                return null;
//            }
//        }.execute();
//
//        return dataManager.getDbRepo().getStudentDAO().getActiveStudentDetails();
//    }
//
//    public LiveLocationData<List<Parent>> getParentsDetails() {
//        MutableLiveData<List<Parent>> data = new MutableLiveData<>();
//
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                data.postValue(dataManager.getDbRepo().getUserParentDAO().getActiveStudentParentDetails());
//                return null;
//            }
//        }.execute();
//
//
//        return data;
//    }
}
