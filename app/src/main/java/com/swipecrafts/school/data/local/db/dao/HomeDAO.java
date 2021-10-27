package com.swipecrafts.school.data.local.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.swipecrafts.school.data.model.db.Event;
import com.swipecrafts.school.data.model.db.Notification;
import com.swipecrafts.school.ui.home.HomeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 5/20/2018.
 */

@Dao
public abstract class HomeDAO {

    @Query("SELECT * from notifications WHERE is_pinned =:type")
    public abstract LiveData<List<Notification>> getNotices(int type);

    @Query("SELECT * from events")
    public abstract LiveData<List<Event>> getEvents();

    @Transaction
    public LiveData<List<HomeModel>> getHomeData(String userType) {
        MediatorLiveData<List<HomeModel>> data = new MediatorLiveData<>();
        List<HomeModel> homeItemList = new ArrayList<>();
        data.setValue(homeItemList);
//
//        AtomicInteger isBothFetched = new AtomicInteger();
//        isBothFetched.set(0);
//
//        LiveLocationData<List<Notification>> noticeSource = getNotices(UserType.from(userType) == UserType.TEACHER ? 1 : 0);
//        LiveLocationData<List<Event>> eventSource = getEvents();
//
//        data.addSource(noticeSource, notifications -> {
//            isBothFetched.set(50);
//            if (notifications != null) homeItemList.addAll(notifications);
//
//            if (isBothFetched.get() == 100) {
//                Collections.sort(homeItemList);
//                data.setValue(homeItemList);
//                isBothFetched.set(0);
//            }
//        });
//        data.addSource(eventSource, events -> {
//            isBothFetched.set(50);
//            if (events != null) homeItemList.addAll(events);
//
//            if (isBothFetched.get() == 100) {
//                Collections.sort(homeItemList);
//                data.setValue(homeItemList);
//                isBothFetched.set(0);
//            }
//        });

        return data;
    }
}
