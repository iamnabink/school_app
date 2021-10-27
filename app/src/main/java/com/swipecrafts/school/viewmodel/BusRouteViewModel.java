package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.model.db.Bus;
import com.swipecrafts.school.data.model.db.BusRoute;
import com.swipecrafts.school.data.remote.ApiListener;
import com.swipecrafts.school.utils.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/15/2018.
 */

public class BusRouteViewModel extends ViewModel {
    private DataManager dataManager;

    public BusRouteViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public LiveData<Integer> countBus() {
        return dataManager.getDbRepo().getBusDAO().count();
    }

    public int countBusRoute(long busId) {
        return dataManager.getDbRepo().getBusRouteDAO().countStaticByBus(busId);
    }

    public LiveData<List<Bus>> getBusList() {
        return dataManager.getDbRepo().getBusDAO().getAllBus();
    }

    public LiveData<Bus> getBus(long id) {
        return dataManager.getDbRepo().getBusDAO().getBusById(id);
    }

    public LiveData<List<BusRoute>> getBusRoutes(long busId) {
        return dataManager.getDbRepo().getBusRouteDAO().getBusRouteByBus(busId);
    }

    public LiveData<Pair<String, Boolean>> refreshBusRouteList(long busId) {
        MutableLiveData<Pair<String, Boolean>> liveData = new MutableLiveData<>();
        dataManager.getApiRepo().getBusRouteList(String.valueOf(busId), new ApiListener<List<BusRoute>>() {
            @Override
            public void onSuccess(List<BusRoute> response) {

                for (BusRoute route : response) {
                    route.setBusId(busId);
                }
                Log.e("busRoutes", response.size() + "");
                new AsyncTask<List<BusRoute>, Void, Void>() {
                    @Override
                    protected Void doInBackground(List<BusRoute>... args) {
                        List<Long> ids = dataManager.getDbRepo().getBusRouteDAO().insertAll(response);
                        Log.e("insertedBusRoute", Arrays.toString(ids.toArray()));
                        return null;
                    }
                }.execute(response);
                liveData.postValue(new Pair<>("Success!!", true));
            }

            @Override
            public void onFailed(String message, int code) {
                message = (code == 1001 ? dataManager.isNetworkConntected() ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : message);
                liveData.postValue(new Pair<>(message, false));
            }
        });
        return liveData;
    }

    public LiveData<Pair<String, Boolean>> refreshBusList() {
        MutableLiveData<Pair<String, Boolean>> liveData = new MutableLiveData<>();
        dataManager.getApiRepo().getBusList(new ApiListener<List<Bus>>() {
            @Override
            public void onSuccess(List<Bus> response) {
                new AsyncTask<List<Bus>, Void, Void>() {
                    @Override
                    protected Void doInBackground(List<Bus>... args) {
                        List<Long> ids = dataManager.getDbRepo().getBusDAO().insertAll(args[0]);
                        Log.e("insertedBus", Arrays.toString(ids.toArray()));

                        liveData.postValue(new Pair<>("Success!!", true));
                        return null;
                    }
                }.execute(response);
            }

            @Override
            public void onFailed(String message, int code) {
                message = (code == 1001 ? dataManager.isNetworkConntected() ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : message);

                liveData.postValue(new Pair<>(message, false));
            }
        });

        return liveData;
    }
}
