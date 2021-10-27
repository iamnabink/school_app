package com.swipecrafts.school.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.manager.NetworkBoundResource;
import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.api.UserType;
import com.swipecrafts.school.data.model.db.BusDriver;
import com.swipecrafts.school.data.model.db.User;
import com.swipecrafts.school.data.remote.AppApiRepository;
import com.swipecrafts.school.ui.dashboard.livebus.model.LiveBus;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 5/20/2018.
 */
public class UserRepository extends BaseRepository{

    public UserRepository(AppApiRepository webService, AppDbRepository localService, AppPreferencesRepository prefService) {
        super(webService, localService, prefService);
    }

    public LiveData<Resource<User>> checkUserLogin(String username, String password, UserType userType, String fcmKey){
        return new NetworkBoundResource<User, User>() {
            boolean hasLocalUser = false;
            @Override
            protected void saveCallResult(@NonNull User item) {
                if (userType == UserType.TEACHER || userType == UserType.DRIVER){
                    dbService.getUserParentDAO().deleteAll();
                    dbService.getUserBookDAO().deleteAll();
                    dbService.getUserDAO().deleteAll();
                    dbService.getParentDAO().deleteAll();
                    dbService.getBookDAO().deleteAll();
                    dbService.getStudentDAO().deleteAll();
                }

                Log.e("Login Success", item.toString());
                item.setName(username);
                dbService.getUserDAO().insertAsActive(item);
                preferencesService.setProtectedApiHeader(item);
                webService.updateAPIHeaders();
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return data == null;
            }

            @Override
            protected boolean shouldReturnLocalData() {
                return false;
            }

            @Override
            protected boolean validateLocalData(User item) {
                hasLocalUser = (item != null);
                Log.e("HasLocal", hasLocalUser+" check ");
                return !hasLocalUser;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return dbService.getUserDAO().getUserByUserName(username);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return webService.loginUser(username, password, userType, fcmKey);
            }

            @Override
            protected Resource<User> createErrorResponse(int code, String message, @Nullable User item) {
                if (hasLocalUser) message = "You have already logged in!!";
                return super.createErrorResponse(code, message, item);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<User>>> loadUsers() {
        MediatorLiveData<Resource<List<User>>> resource = new MediatorLiveData<>();
        resource.addSource(dbService.getUserDAO().getUserList(), users -> resource.setValue(Resource.success(users)));
        return resource;
    }

    public LiveData<Resource<User>> logOutUser() {
        return new NetworkBoundResource<User, Boolean>() {

            @Override
            protected void saveCallResult(@NonNull Boolean item) {
                if (item){
                    if (UserType.from(preferencesService.getUserType()) == UserType.PARENT){
                        dbService.getNotificationDAO().deleteByCategory(1);
                    }
                    User actUser = dbService.getUserDAO().makeNewActiveUser();
                    preferencesService.setProtectedApiHeader(actUser);
                    webService.updateAPIHeaders();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return (data != null);
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return dbService.getUserDAO().getActiveUser();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Boolean>> createCall() {
                return webService.logOutUser();
            }
        }.getAsLiveData();
    }

    public LiveData<User> loadActiveUser() {
        return dbService.getUserDAO().getActiveUser();
    }

    public void changeActiveUser(User currentUser) {
        new AsyncTask<User, Void, Void>() {
            @Override
            protected Void doInBackground(User... args) {
                currentUser.isActive(1);
                dbService.getUserDAO().resetActiveUser();
                dbService.getUserDAO().update(currentUser);
                preferencesService.setProtectedApiHeader(currentUser);
                webService.updateAPIHeaders();
                return null;
            }
        }.execute(currentUser);
    }

    public boolean isUserLoggedIn() {
        return preferencesService.getLoggedMode() == DataManager.LoggedInMode.LOGGED_IN_MODE.value();
    }

    public LiveData<ApiResponse<Boolean>> changePassword(String oldPwd, String newPwd, String confPwd) {
        return webService.changePassword(oldPwd, newPwd, confPwd);
    }

    public LiveData<ApiResponse<List<LiveBus>>> getLiveMapBusList() {
        return webService.getLiveMapBusList();
    }

    public LiveData<Resource<List<BusDriver>>> getBusDetails() {
        return new NetworkBoundResource<List<BusDriver>, List<BusDriver>>() {
            @Override
            protected void saveCallResult(@NonNull List<BusDriver> item) {
                dbService.getBusDriverDAO().insertAll(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<BusDriver> data) {
                return data== null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<BusDriver>> loadFromDb() {
                return dbService.getBusDriverDAO().getBusDriverByDriverId(preferencesService.getProtectedApiHeader().getSESUId());
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<BusDriver>>> createCall() {
                return webService.getBusDetails();
            }
        }.getAsLiveData();
    }

    public String getUserType() {
        return preferencesService.getUserType();
    }

    public long getUserId() {
        return preferencesService.getProtectedApiHeader().getSESUId();
    }

    public String getSchoolId() {
        return preferencesService.getSchoolId();
    }
}
