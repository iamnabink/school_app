package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.model.others.VMResponse;
import com.swipecrafts.school.data.remote.ApiListener;
import com.swipecrafts.school.utils.Constants;

/**
 * Created by Madhusudan Sapkota on 4/2/2018.
 */
public class LeaveApplicationViewModel extends ViewModel {
    private DataManager dataManager;

    public LeaveApplicationViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public MutableLiveData<VMResponse<String>> postLeaveApplication(String title, String subject, String body){
        MutableLiveData<VMResponse<String>> data = new MutableLiveData<>();
        dataManager.getApiRepo().submitLeaveApplication(title, subject, body, new ApiListener<String>() {
            @Override
            public void onSuccess(String response) {
                data.postValue(new VMResponse<>(true, "Success", response));
            }

            @Override
            public void onFailed(String message, int code) {
                message = (code == 1001 ? dataManager.isNetworkConntected() ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : message);

                data.postValue(new VMResponse<>(false, message, null));
            }
        });

        return data;
    }
}
