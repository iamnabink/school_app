package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.model.db.Chapter;
import com.swipecrafts.school.data.remote.ApiListener;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.listener.TaskListener;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/5/2018.
 */

public class ChapterViewModel extends ViewModel {
    private DataManager dataManager;

    public ChapterViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public int getChapterNumber(long subjectID) {
        return dataManager.getDbRepo().getChapterDAO().countStaticBySubject(subjectID);
    }

    public LiveData<List<Chapter>> getChaptersBySubject(long subjectID) {
        return dataManager.getDbRepo().getChapterDAO().getChaptersBySubject(subjectID);
    }

    public MutableLiveData<Pair<String, Boolean>> refreshChapters(long subjectID) {
        MutableLiveData<Pair<String, Boolean>> liveResult = new MutableLiveData<>();
        liveResult.postValue(null);

        dataManager.getApiRepo().getChapterList(String.valueOf(subjectID), new ApiListener<List<Chapter>>() {

            @Override
            public void onSuccess(List<Chapter> response) {
                for (Chapter item : response) item.setSubjectId(subjectID);

                new InsertTask(completed -> {
                    if (response.isEmpty())
                        liveResult.postValue(new Pair<>("Sorry!! there is no data available.", false));
                    else
                        liveResult.postValue(new Pair<>("Success!!", true));
                }).execute(response);
            }

            @Override
            public void onFailed(String message, int code) {
                message = (code == 1001 ? dataManager.isNetworkConntected() ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : message);
                Log.e("chapterFailed", message);
                liveResult.postValue(new Pair<>(message, false));
            }
        });

        return liveResult;
    }

    private class InsertTask extends AsyncTask<List<Chapter>, Void, Void> {

        private TaskListener listener;

        public InsertTask(TaskListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(List<Chapter>... args) {
            dataManager.getDbRepo().getChapterDAO().deleteAll();
            dataManager.getDbRepo().getChapterDAO().insertAll(args[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listener.onCompleted(true);
        }
    }
}
