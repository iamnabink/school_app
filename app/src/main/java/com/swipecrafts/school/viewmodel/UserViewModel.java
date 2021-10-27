package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.model.api.UserType;
import com.swipecrafts.school.data.model.db.BusDriver;
import com.swipecrafts.school.data.model.db.User;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.school.data.repository.UserRepository;
import com.swipecrafts.school.ui.dashboard.livebus.model.LiveBus;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 2/20/2018.
 */

public class UserViewModel extends ViewModel {

    private final RepositoryFactory repositoryFactory;
    private final UserRepository userRepository;

    public UserViewModel(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
        this.userRepository = repositoryFactory.create(UserRepository.class);
    }

    public LiveData<User> getActiveUser() {
        return userRepository.loadActiveUser();
    }

    public void changeActiveUser(User currentUser) {
        userRepository.changeActiveUser(currentUser);
    }

    public LiveData<Resource<User>> loginUser(String username, String password, UserType type, String fcmKey) {
        return userRepository.checkUserLogin(username, password, type, fcmKey);
    }

    public LiveData<Resource<List<User>>> getUsers(){
        return userRepository.loadUsers();
    }

    public LiveData<Resource<User>> logOutUser(){
        return userRepository.logOutUser();
    }

    public LiveData<ApiResponse<Boolean>> changePassword(String oldPwd, String newPwd, String confPwd) {
        return userRepository.changePassword(oldPwd, newPwd, confPwd);
    }

    public LiveData<Resource<List<BusDriver>>> getBusDetails() {
        return userRepository.getBusDetails();
    }

    public LiveData<ApiResponse<List<LiveBus>>> getLiveMapBusList() {
        return userRepository.getLiveMapBusList();
    }

    public String getUserType() {
        return userRepository.getUserType();
    }

    public long getUserId() {
        return userRepository.getUserId();
    }

    public String getSchoolId(){
        return userRepository.getSchoolId();
    }
}
