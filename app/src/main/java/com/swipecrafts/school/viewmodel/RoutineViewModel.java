package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.model.others.VMResponse;
import com.swipecrafts.school.data.remote.ApiListener;
import com.swipecrafts.school.ui.dashboard.examroutine.model.ExamRoutineRes;
import com.swipecrafts.school.ui.dashboard.schedule.model.ClassResponse;
import com.swipecrafts.school.utils.Constants;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/25/2018.
 */

public class RoutineViewModel extends ViewModel {
    private DataManager dataManager;

    public RoutineViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public MutableLiveData<VMResponse<List<ClassResponse>>> refreshClassRoutine(){
        MutableLiveData<VMResponse<List<ClassResponse>>> data = new MutableLiveData<>();

        dataManager.getApiRepo().getClassRoutine(new ApiListener<List<ClassResponse>>() {
            @Override
            public void onSuccess(List<ClassResponse> response) {
                if (response == null || response.isEmpty()){
                    data.postValue(new VMResponse<>(false, "No daily routine found.", response));
                }else {
                    data.postValue(new VMResponse<>(true, "Success", response));
                }
            }

            @Override
            public void onFailed(String message, int code) {
                message = (code == 1001 ? dataManager.isNetworkConntected() ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : message);

                data.postValue(new VMResponse<>(false, message, null));
            }
        });

        return data;
    }

    public MutableLiveData<VMResponse<List<ExamRoutineRes>>> refreshExamRoutine() {
        MutableLiveData<VMResponse<List<ExamRoutineRes>>> data = new MutableLiveData<>();

        dataManager.getApiRepo().getExamRoutine(new ApiListener<List<ExamRoutineRes>>() {
            @Override
            public void onSuccess(List<ExamRoutineRes> response) {
                if (response == null || response.isEmpty()){
                    data.postValue(new VMResponse<>(false, "No exam routine found.", response));
                }else {
                    data.postValue(new VMResponse<>(true, "Success", response));
                }
            }

            @Override
            public void onFailed(String message, int code) {
                if (code == 1001 && dataManager.isNetworkConntected())
                    message = "sorry an unexpected error occurred!";
                data.postValue(new VMResponse<>(false, message, null));
            }
        });

        return data;
    }
}
