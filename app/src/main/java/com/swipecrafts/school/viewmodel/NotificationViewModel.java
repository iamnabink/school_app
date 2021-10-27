package com.swipecrafts.school.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.api.UserType;
import com.swipecrafts.school.data.model.db.Notification;
import com.swipecrafts.school.data.repository.NotificationRepository;
import com.swipecrafts.school.data.repository.RepositoryFactory;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 2/20/2018.
 */

public class NotificationViewModel extends ViewModel {
    private final RepositoryFactory repositoryFactory;
    private NotificationRepository repo;

    public NotificationViewModel(RepositoryFactory repositoryFactory) {
        this.repositoryFactory = repositoryFactory;
        this.repo = repositoryFactory.create(NotificationRepository.class);
    }

    public UserType getCurrentUserType(){
        return repo.loadCurrentUserType();
    }

    public LiveData<Resource<List<Notification>>> getNotificationsByType(UserType type, boolean loadFromRemote) {
        return repo.loadNotification(type, loadFromRemote);
    }

    public static void main(String[] args) {
        System.out.println((false || false || false));
    }
}
