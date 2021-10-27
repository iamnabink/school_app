package com.swipecrafts.school.data.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.manager.NetworkBoundResource;
import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.api.UserType;
import com.swipecrafts.school.data.model.db.Notification;
import com.swipecrafts.school.data.remote.AppApiRepository;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 5/20/2018.
 */
public class NotificationRepository extends BaseRepository {

    public NotificationRepository(AppApiRepository webService, AppDbRepository localService, AppPreferencesRepository preferencesRepository) {
        super(webService, localService, preferencesRepository);
    }

    public LiveData<Resource<List<Notification>>> loadNotification(UserType userType, boolean loadFromRemote){
        return new NetworkBoundResource<List<Notification>, List<Notification>>() {
            @Override
            protected void saveCallResult(@NonNull List<Notification> item) {
                for (Notification notice : item) notice.setIsPinned(userType == UserType.PARENT ? 0 : 1);
                dbService.getNotificationDAO().deleteAndInsert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Notification> data) {
                return (loadFromRemote || data == null || data.isEmpty() || isDataFresh(data));
            }

            @NonNull
            @Override
            protected LiveData<List<Notification>> loadFromDb() {
                return dbService.getNotificationDAO().getNotificationsByCategory(userType == UserType.PARENT ? 0 : 1);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Notification>>> createCall() {
                return webService.getNotifications(userType);
            }
        }.getAsLiveData();
    }

    private boolean isDataFresh(List<Notification> data) {
        boolean hasTeacherData = false;
        boolean isNeededTeacherData = loadCurrentUserType() == UserType.PARENT;
        if (isNeededTeacherData){
            for (Notification item : data) {
                if (item.getIsPinned() == 1) {
                    hasTeacherData = true;
                    break;
                }
            }
        }
        return hasTeacherData;
    }

    public UserType loadCurrentUserType() {
        return UserType.from(preferencesService.getUserType());
    }
}
