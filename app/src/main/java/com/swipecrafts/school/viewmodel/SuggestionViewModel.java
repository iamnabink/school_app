package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Pair;

import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.remote.ApiListener;
import com.swipecrafts.school.utils.Constants;

/**
 * Created by Madhusudan Sapkota on 3/29/2018.
 */
public class SuggestionViewModel extends ViewModel {
    private DataManager dataManager;

    public SuggestionViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    public LiveData<Pair<String, Boolean>> postSuggestion(String title, String body, int isAnonymous) {
        MutableLiveData<Pair<String, Boolean>> result = new MutableLiveData<>();

        this.dataManager.getApiRepo().submitSuggestion(title, body, isAnonymous, new ApiListener<String>() {
            @Override
            public void onSuccess(String response) {
                result.postValue(new Pair<>(response, true));
            }

            @Override
            public void onFailed(String message, int code) {
                message = (code == 1001 ? dataManager.isNetworkConntected() ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : message);

                result.postValue(new Pair<>(message, false));
            }
        });

        return result;
    }

    public LiveData<Pair<String, Boolean>> postAppreciation(String title, String body, int isAnonymous) {
        MutableLiveData<Pair<String, Boolean>> result = new MutableLiveData<>();

        this.dataManager.getApiRepo().submitAppreciation(title, body, isAnonymous, new ApiListener<String>() {
            @Override
            public void onSuccess(String response) {
                result.postValue(new Pair<>(response, true));
            }

            @Override
            public void onFailed(String message, int code) {
                message = (code == 1001 ? dataManager.isNetworkConntected() ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : message);

                result.postValue(new Pair<>(message, false));
            }
        });

        return result;
    }
}
