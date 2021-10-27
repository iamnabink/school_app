package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.model.db.SchoolSchedule;
import com.swipecrafts.school.data.remote.ApiListener;
import com.swipecrafts.school.utils.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/14/2018.
 */

public class SchoolScheduleViewModel extends ViewModel{
    private DataManager dataManager;

    public SchoolScheduleViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    public LiveData<Integer> countSchoolSchedule(){
        return dataManager.getDbRepo().getSchoolScheduleDAO().count();
    }

    public LiveData<List<SchoolSchedule>> getSchoolSchedule() {
        return dataManager.getDbRepo().getSchoolScheduleDAO().getAllSchoolSchedule();
    }

    public MutableLiveData<Pair<String, Boolean>> refersSchoolSchedule(){
        MutableLiveData<Pair<String, Boolean>> result = new MutableLiveData<>();


        dataManager.getApiRepo().getSchoolSchedule(new ApiListener<List<SchoolSchedule>>() {
            @Override
            public void onSuccess(List<SchoolSchedule> response) {
                new AsyncTask<List<SchoolSchedule>, Void, Void>() {
                    @Override
                    protected Void doInBackground(List<SchoolSchedule>...args) {
                       List<Long> ids =  dataManager.getDbRepo().getSchoolScheduleDAO().insertAll(args[0]);

                       result.postValue(new Pair<>("Success!!", true));
                        Log.e("SchoolRoutineIDs", Arrays.toString(ids.toArray()));
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
