package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.model.db.DashboardItem;
import com.swipecrafts.school.data.remote.ApiListener;

import java.util.List;


/**
 * Created by Madhusudan Sapkota on 3/2/2018.
 */

public class DashboardViewModel extends ViewModel {
    private DataManager dataManager;

    public DashboardViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public LiveData<List<DashboardItem>> getMenuItem(){
        return dataManager.getDbRepo().getMenuDAO().getAllMenus();
    }

    public void checkNewAppVersion(ApiListener<String> listener) {
        dataManager.getApiRepo().checkAppVersion(listener);
    }
}
