package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.school.data.repository.SessionRepository;

/**
 * Created by Madhusudan Sapkota on 2/23/2018.
 */

public class SessionViewModel extends ViewModel {

    private final RepositoryFactory repositoryFactory;
    private final SessionRepository sessionRepository;

    public SessionViewModel(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
        this.sessionRepository = repositoryFactory.create(SessionRepository.class);
    }

    public LiveData<Resource<String>> loadSchoolKey(){
        return sessionRepository.loadSchoolKey();
    }

    public LiveData<Resource<SessionRepository.SessionResources>> loadImportantDataFromRemote(){
        return sessionRepository.loadImportantData();
    }

    public String loadUserType() {
        return sessionRepository.getUserStatus();
    }
}
