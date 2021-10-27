package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.repository.DigitalClassRepository;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.school.ui.dc.dcfeed.ContentFeed;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/22/2018.
 */

public class ContentFeedViewModel extends ViewModel {
    private final DigitalClassRepository digitalClassRepository;
    private RepositoryFactory dataManager;

    public ContentFeedViewModel(RepositoryFactory dataManager) {
        this.dataManager = dataManager;
        this.digitalClassRepository = dataManager.create(DigitalClassRepository.class);
    }

    public LiveData<ApiResponse<List<ContentFeed>>> loadDCContentFeed(long gradeId, long subjectId, long chapterId){
        return digitalClassRepository.getDigitalContentFeed(gradeId, subjectId, chapterId);
    }
}
