package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.model.db.DCSubject;
import com.swipecrafts.school.data.remote.ApiListener;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.listener.TaskListener;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/5/2018.
 */

public class SubjectViewModel extends ViewModel {

    private DataManager dataManager;
    private int subjectSuccessList;

    public SubjectViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public int getSubjectNumber(long gradeId) {
        return dataManager.getDbRepo().getSubjectDAO().countStaticByGrade(gradeId);
    }

    public LiveData<List<DCSubject>> getSubjectsByGrade(long gradeId) {
        return dataManager.getDbRepo().getSubjectDAO().getSubjectsByGrade(gradeId);
    }

    public MutableLiveData<Pair<String, Boolean>> refreshSubjects(String gradeId) {
        MutableLiveData<Pair<String, Boolean>> liveResult = new MutableLiveData<>();

        dataManager.getApiRepo().getSubjectList(gradeId, new ApiListener<List<DCSubject>>() {

            @Override
            public void onSuccess(List<DCSubject> response) {
                for (DCSubject DCSubject : response) DCSubject.setGradeID(gradeId);
                new InsertTask(completed -> {
                    if (response.isEmpty())
                        liveResult.postValue(new Pair<>("Sorry!! there is no data available.", false));
                    else
                        liveResult.postValue(new Pair<>("Success!!", true));
                    Log.e("subjects", "inserted");
                }).execute(response);
            }

            @Override
            public void onFailed(String message, int code) {
                message = (code == 1001 ? dataManager.isNetworkConntected() ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : message);

                liveResult.postValue(new Pair<>(message, false));
            }
        });

        return liveResult;
    }

    private LiveData<Integer> updateSubjects(List<DCSubject> DCSubjects) {
        MutableLiveData<Integer> progress = new MutableLiveData<>();
        progress.postValue(0);

        for (DCSubject item : DCSubjects) {

            new UpdateTask(completed -> {
                if (completed) progress.postValue(progress.getValue() + 1);
            }).execute(item);
        }

        return progress;
    }

    private class InsertTask extends AsyncTask<List<DCSubject>, Void, Void> {

        private TaskListener listener;

        InsertTask(TaskListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(List<DCSubject>...args) {
            dataManager.getDbRepo().getSubjectDAO().deleteAll();
            List<Long> ids = dataManager.getDbRepo().getSubjectDAO().insertAll(args[0]);
            Log.e("InsertedSubject", Arrays.toString(ids.toArray()));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listener.onCompleted(true);
        }
    }

    private class UpdateTask extends AsyncTask<DCSubject, Void, Void> {

        private TaskListener listener;

        UpdateTask(TaskListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(DCSubject... DCSubjects) {
            dataManager.getDbRepo().getSubjectDAO().update(DCSubjects[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void completed) {
            super.onPostExecute(completed);
            listener.onCompleted(true);
        }
    }
}
