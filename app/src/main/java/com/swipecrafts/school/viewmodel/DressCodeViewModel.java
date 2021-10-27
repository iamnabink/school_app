package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Pair;

import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.model.db.DressCode;
import com.swipecrafts.school.data.model.db.Gender;
import com.swipecrafts.school.data.remote.ApiListener;
import com.swipecrafts.school.utils.Constants;

import java.io.File;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/16/2018.
 */

public class DressCodeViewModel extends ViewModel {

    MutableLiveData<Pair<File, Double>> result;
    private DataManager dataManager;


    public DressCodeViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public int countStaticDressCode() {
        return dataManager.getDbRepo().getGenderDAO().countStatic();
    }

    public LiveData<Integer> countDressCode() {
        return dataManager.getDbRepo().getGenderDAO().count();
    }

    public LiveData<List<Gender>> getAllDressCode() {
        return dataManager.getDbRepo().getGenderDAO().getAllGender();
    }

    public MutableLiveData<Pair<String, Boolean>> refreshDressCode() {

        MutableLiveData<Pair<String, Boolean>> result = new MutableLiveData<>();
        result.postValue(null);

        dataManager.getApiRepo().getDressCode(new ApiListener<List<DressCode>>() {
            @Override
            public void onSuccess(List<DressCode> response) {
                new AsyncTask<List<DressCode>, Void, Void>() {
                    @Override
                    protected Void doInBackground(List<DressCode>... args) {
                        dataManager.getDbRepo().getGenderDAO().deleteAll();
                        for (DressCode code : args[0]) {
                            for (Gender gender : code.getGender()) {
                                gender.setDayName(code.getDay());
                            }
                            List<Long> ids = dataManager.getDbRepo().getGenderDAO().insertAll(code.getGender());
                        }
                        result.postValue(new Pair<>("Success!!", true));
                        return null;
                    }
                }.execute(response);
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
