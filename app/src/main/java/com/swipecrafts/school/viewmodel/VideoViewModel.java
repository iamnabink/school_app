package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.school.data.repository.DigitalClassRepository;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.school.ui.dc.Video.VideoFeed;


import java.util.List;

public class VideoViewModel extends ViewModel {

    private final DigitalClassRepository digitalClassRepository;
    private RepositoryFactory dataManager;

    public VideoViewModel(RepositoryFactory dataManager) {
        this.dataManager = dataManager;
        this.digitalClassRepository = dataManager.create(DigitalClassRepository.class);
    }

    public LiveData<ApiResponse<List<VideoFeed>>> loadDCVideoFeed(long gradeId, long subjectId){
        return digitalClassRepository.getVideoContentFeed(gradeId, subjectId);
    }
}
