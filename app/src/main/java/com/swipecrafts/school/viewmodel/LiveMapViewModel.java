package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.location.Location;

import com.swipecrafts.school.data.repository.LocationRepository;
import com.swipecrafts.school.data.repository.RepositoryFactory;

/**
 * Created by Madhusudan Sapkota on 7/23/2018.
 */
public class LiveMapViewModel extends ViewModel {

    private final LocationRepository locationRepository;

    public LiveMapViewModel(RepositoryFactory factory) {
        this.locationRepository = factory.create(LocationRepository.class);
    }

    public void broadCastMyLocation(Location location) {
        this.locationRepository.postLocation(location);
    }
}
