package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.db.Grade;
import com.swipecrafts.school.data.model.db.User;
import com.swipecrafts.school.data.repository.DigitalClassRepository;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.school.data.repository.UserRepository;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/4/2018.
 */

public class GradeViewModel extends ViewModel {

    private final DigitalClassRepository digitalClassRepo;
    private final RepositoryFactory repositoryFactory;
    private final UserRepository userRepository;

    public GradeViewModel(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
        this.digitalClassRepo = repositoryFactory.create(DigitalClassRepository.class);
        this.userRepository = repositoryFactory.create(UserRepository.class);
    }

    public LiveData<Resource<List<Grade>>> getGrades(boolean fetchFromRemote) {
        return digitalClassRepo.loadGrades(fetchFromRemote);
    }

    public boolean isUserLoggedIn() {
        return userRepository.isUserLoggedIn();
    }

    public LiveData<User> getActiveUser() {
        return userRepository.loadActiveUser();
    }
}
