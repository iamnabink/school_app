package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.DataManager;

/**
 * Created by Madhusudan Sapkota on 3/5/2018.
 */

public class LocalDataViewModel extends ViewModel{
    private DataManager dataManager;

    public LocalDataViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public int getEventsNumber(){
        return dataManager.getDbRepo().getEventDAO().countStatic();
    }

    public int getNotificationsNumber(){
        return dataManager.getDbRepo().getNotificationDAO().countStatic();
    }

    public int getDCGradesNumber(){
        return dataManager.getDbRepo().getGradeDAO().countStatic();
    }

    public int getMenuItemsNumber(){
        return dataManager.getDbRepo().getMenuDAO().countStatic();
    }
}
