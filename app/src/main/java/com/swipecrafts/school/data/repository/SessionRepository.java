package com.swipecrafts.school.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.manager.NetworkBoundResource;
import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.db.DashboardItem;
import com.swipecrafts.school.data.model.db.Event;
import com.swipecrafts.school.data.model.db.Grade;
import com.swipecrafts.school.data.model.db.Notification;
import com.swipecrafts.school.data.remote.AppApiRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Madhusudan Sapkota on 5/21/2018.
 */
public class SessionRepository extends BaseRepository {

    public SessionRepository(AppApiRepository webService, AppDbRepository dbService, AppPreferencesRepository preferencesRepository) {
        super(webService, dbService, preferencesRepository);
    }

    public LiveData<Resource<String>> loadSchoolKey(){
        return new NetworkBoundResource<String, String>() {
            @Override
            protected void saveCallResult(@NonNull String item) {
                Log.e("New Data", "School Id saving "+ item);
                preferencesService.setSchoolId(item);
                webService.updateAPIHeaders();
            }

            @Override
            protected boolean shouldFetch(@Nullable String data) {
                return preferencesService.getSchoolId() == null;
            }

            @NonNull
            @Override
            protected LiveData<String> loadFromDb() {
                MutableLiveData<String> schoolId = new MutableLiveData<>();
                schoolId.postValue(preferencesService.getSchoolId());
                return schoolId;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<String>> createCall() {
                return webService.getSchoolId();
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<SessionResources>> loadImportantData() {
        return new NetworkBoundResource<SessionResources, SessionResources>() {
            boolean loadMenu = false, loadDC = false, loadNotice = false, loadEvent = false;

            @Override
            protected void saveCallResult(@NonNull SessionResources item) {
                Log.e("Save Data", "data saving "+item.hasAllData());

                dbService.getMenuDAO().deleteAndInsert(item.dashboardItems);
                dbService.getGradeDAO().deleteAndInsert(item.digitalClass);
                dbService.getNotificationDAO().deleteAndInsert(item.notifications);
                dbService.getEventDAO().deleteAndInsert(item.events);
            }

            @Override
            protected boolean shouldFetch(@Nullable SessionResources data) {
                if (data != null) {
                    loadMenu = data.hasDashboardItems();
                    loadDC = data.hasDigitalClass();
                    loadNotice = data.hasNotifications();
                    loadEvent = data.hasEvents();
                    Log.e("New Data", "Has all data "+ data.hasAllData());
                }

                return (data == null || !data.hasAllData());
            }

            @Override
            protected boolean shouldReturnLocalData() {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<SessionResources> loadFromDb() {
                MediatorLiveData<SessionResources> data = new MediatorLiveData<>();
                SessionResources resources = new SessionResources();
                final int total = 4;
                AtomicInteger process = new AtomicInteger();
                process.set(0);
                data.addSource(dbService.getMenuDAO().getAllMenus(), items -> {
                    resources.setMenuItems(items);
                    process.set(process.get() + 1);

                    if (process.get() == total){
                        data.setValue(resources);
                    }
                });

                data.addSource(dbService.getGradeDAO().getAllGrades(), items -> {
                    resources.setDigitalClassItems(items);
                    process.set(process.get() + 1);
                    if (process.get() == total){
                        data.setValue(resources);
                    }
                });

                data.addSource(dbService.getNotificationDAO().getAllNotifications(), items -> {
                    resources.setNoticeItems(items);
                    process.set(process.get() + 1);
                    if (process.get() == total){
                        data.setValue(resources);
                    }
                });

                data.addSource(dbService.getEventDAO().getAllEvents(), items -> {
                    resources.setEventItems(items);
                    process.set(process.get() + 1);
                    if (process.get() == total){
                        data.setValue(resources);
                    }
                });
                return data;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<SessionResources>> createCall() {
                return webService.getSessionData(!loadMenu, !loadDC, !loadNotice, !loadEvent);
            }
        }.getAsLiveData();
    }

    public String getUserStatus() {
        return preferencesService.getUserType();
    }

    public static class SessionResources {
        List<DashboardItem> dashboardItems;
        List<Grade> digitalClass;
        List<Notification> notifications;
        List<Event> events;

        public SessionResources() {
        }

        public SessionResources(List<DashboardItem> dashboardItems, List<Grade> digitalClass, List<Notification> notifications, List<Event> events) {
            this.dashboardItems = dashboardItems;
            this.digitalClass = digitalClass;
            this.notifications = notifications;
            this.events = events;
        }

        boolean hasAllData() {
            return (hasDashboardItems() && hasDigitalClass() && hasNotifications() && hasEvents());
        }

        public void setMenuItems(List<DashboardItem> menuItems) {
            this.dashboardItems = menuItems;
        }

        public void setDigitalClassItems(List<Grade> digitalClassItems) {
            this.digitalClass = digitalClassItems;
        }

        public void setNoticeItems(List<Notification> noticeItems) {
            this.notifications = noticeItems;
        }

        public void setEventItems(List<Event> eventItems) {
            this.events = eventItems;
        }

        public boolean hasDashboardItems() {
            return (dashboardItems !=null && !dashboardItems.isEmpty());
        }

        public boolean hasDigitalClass() {
            return (digitalClass != null && !digitalClass.isEmpty());
        }

        public boolean hasNotifications() {
            return (notifications !=null && !notifications.isEmpty());
        }

        public boolean hasEvents() {
            return (events !=null && !events.isEmpty());
        }
    }
}
