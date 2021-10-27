package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Pair;

import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.model.db.Book;
import com.swipecrafts.school.data.model.db.relation.UserBook;
import com.swipecrafts.school.data.remote.ApiListener;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/20/2018.
 */

public class BookViewModel extends ViewModel {
    private DataManager dataManager;

    public BookViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    public LiveData<Integer> countBooks() {
        return dataManager.getDbRepo().getUserBookDAO().countBooksByActiveUser();
    }

    public LiveData<List<Book>> getBooks() {
        return dataManager.getDbRepo().getUserBookDAO().getBooksByActiveUser();
    }

    public MutableLiveData<Pair<String, Boolean>> refreshBooks() {
        MutableLiveData<Pair<String, Boolean>> result = new MutableLiveData<>();

        dataManager.getApiRepo().getBooks(new ApiListener<List<Book>>() {
            @Override
            public void onSuccess(List<Book> response) {

                new AsyncTask<List<Book>, Void, Void>() {
                    @Override
                    protected Void doInBackground(List<Book>... args) {
                        List<Long> ids1 = dataManager.getDbRepo().getBookDAO().insertAll(args[0]);
                       LogUtils.errorLog("books", "books Size! "+ Arrays.toString(ids1.toArray()));

                        List<UserBook> userBooks = new ArrayList<>();

                        for (Book book : args[0]) {
                            UserBook ub = new UserBook(dataManager.getUserId(), book.getBookId());
                            userBooks.add(ub);
                        }

                        List<Long> ids = dataManager.getDbRepo().getUserBookDAO().insertAll(userBooks);
//                        Log.e("books", "userBooks Size! "+ Arrays.toString(ids.toArray()));

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
