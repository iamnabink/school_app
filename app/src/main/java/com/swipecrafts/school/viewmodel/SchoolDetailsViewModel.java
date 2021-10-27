package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.db.SchoolDetails;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.school.data.repository.SchoolRepository;

/**
 * Created by Madhusudan Sapkota on 3/19/2018.
 */

public class SchoolDetailsViewModel extends ViewModel {
    private final SchoolRepository schoolRepository;
    private RepositoryFactory dataManager;

    public SchoolDetailsViewModel(RepositoryFactory dataManager) {
        this.dataManager = dataManager;
        this.schoolRepository = dataManager.create(SchoolRepository.class);
    }

    public LiveData<Resource<SchoolDetails>> loadSchoolDetails(boolean fetchFromNetwork){
        return schoolRepository.loadSchoolDetails(fetchFromNetwork);
    }
}
