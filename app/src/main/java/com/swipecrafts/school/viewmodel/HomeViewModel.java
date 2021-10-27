package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.repository.HomeTypeRepository;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.school.ui.home.HomeModel;
import com.swipecrafts.library.imageSlider.FeaturesImage;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/12/2018.
 */

public class HomeViewModel extends ViewModel {
    private RepositoryFactory repositoryFactory;
    private HomeTypeRepository repository;

    public HomeViewModel(RepositoryFactory factory) {
        this.repositoryFactory = factory;
        this.repository = repositoryFactory.create(HomeTypeRepository.class);
    }

    public LiveData<Resource<List<HomeModel>>> getHomeItems(boolean loadFromRemote){
        return repository.loadHomeItems(loadFromRemote);
    }

    public LiveData<Resource<List<FeaturesImage>>> getImageUrls(boolean fetchFromRemote) {
        return repository.loadFeatureImages(fetchFromRemote);
    }
}
