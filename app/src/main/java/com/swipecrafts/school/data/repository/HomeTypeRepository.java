package com.swipecrafts.school.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.manager.NetworkBoundResource;
import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.api.UserType;
import com.swipecrafts.school.data.model.db.Event;
import com.swipecrafts.school.data.model.db.Notification;
import com.swipecrafts.school.data.remote.AppApiRepository;
import com.swipecrafts.school.ui.home.HomeModel;
import com.swipecrafts.library.imageSlider.FeaturesImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Madhusudan Sapkota on 5/20/2018.
 */
public class HomeTypeRepository extends BaseRepository {

    public HomeTypeRepository(AppApiRepository webService, AppDbRepository localService, AppPreferencesRepository preferencesRepository) {
        super(webService, localService, preferencesRepository);
    }

    public LiveData<Resource<List<HomeModel>>> loadHomeItems(boolean loadFromRemote) {
        return new NetworkBoundResource<List<HomeModel>, List<HomeModel>>() {

            @Override
            protected void saveCallResult(@NonNull List<HomeModel> items) {
                List<Event> events = new ArrayList<>();
                List<Notification> notices = new ArrayList<>();
                for (HomeModel item : items) {
                    if (item instanceof Notification) {
                        notices.add((Notification) item);
                    } else if (item instanceof Event) {
                        events.add((Event) item);
                    }
                }

                dbService.getNotificationDAO().deleteAndInsert(notices);
                dbService.getEventDAO().deleteAndInsert(events);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<HomeModel> data) {
                return (loadFromRemote || data == null || !isHomeDataFresh(data));
            }

            @NonNull
            @Override
            protected LiveData<List<HomeModel>> loadFromDb() {
                MediatorLiveData<List<HomeModel>> data = new MediatorLiveData<>();
                List<HomeModel> homeItemList = new ArrayList<>();

                AtomicInteger isBothFetched = new AtomicInteger();
                isBothFetched.set(0);

                LiveData<List<Notification>> noticeSource;
                if (UserType.TEACHER.getValue().equals(preferencesService.getUserType())){
                    noticeSource = dbService.getNotificationDAO().getAllNotifications();
                }else{
                   noticeSource = dbService.getNotificationDAO().getNotificationsByCategory(0);
                }

                LiveData<List<Event>> eventSource = dbService.getEventDAO().getAllEvents();

                data.addSource(noticeSource, notifications -> {
                    isBothFetched.set(isBothFetched.get()+50);
                    if (notifications != null) homeItemList.addAll(notifications);

                    if (isBothFetched.get() == 100) {
                        Collections.sort(homeItemList);
                        data.setValue(homeItemList);
                        isBothFetched.set(0);
                    }
                });
                data.addSource(eventSource, events -> {
                    isBothFetched.set(isBothFetched.get()+50);
                    if (events != null) homeItemList.addAll(events);

                    if (isBothFetched.get() == 100) {
                        Collections.sort(homeItemList);
                        data.setValue(homeItemList);
                        isBothFetched.set(0);
                    }
                });

                return data;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<HomeModel>>> createCall() {
                return webService.getHomeItems();
            }
        }.getAsLiveData();
    }

    private boolean isHomeDataFresh(List<HomeModel> items) {
        boolean hasEvent = false;
        boolean hasNotice = false;
        for (HomeModel item : items) {
            if (item instanceof Notification) {
                hasNotice = true;
                if (hasEvent) break;
            } else if (item instanceof Event) {
                hasEvent = true;
                if (hasNotice) break;
            }
        }
        return hasNotice && hasEvent;
    }

    public LiveData<Resource<List<FeaturesImage>>> loadFeatureImages(boolean fetchFromRemote){
        return new NetworkBoundResource<List<FeaturesImage>, List<FeaturesImage>>() {
            @Override
            protected void saveCallResult(@NonNull List<FeaturesImage> item) {
                dbService.getFeaturesImageDAO().deleteAndInsert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<FeaturesImage> data) {
                return (fetchFromRemote || data == null || data.isEmpty());
            }

            @NonNull
            @Override
            protected LiveData<List<FeaturesImage>> loadFromDb() {
                return dbService.getFeaturesImageDAO().getFeatureImages();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<FeaturesImage>>> createCall() {
                return webService.getFeaturesImages();
            }
        }.getAsLiveData();
    }
}
