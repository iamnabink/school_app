package com.swipecrafts.school.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.swipecrafts.school.data.AppDataManager;
import com.swipecrafts.school.data.repository.RepositoryFactory;

import javax.inject.Inject;

/**
 * Created by Madhusudan Sapkota on 2/20/2018.
 */


public class ViewModelFactory implements ViewModelProvider.Factory {

    private final AppDataManager repository;
    private final RepositoryFactory repoFactory;
    private final Application application;

    @Inject
    public ViewModelFactory(Application application, AppDataManager repository, RepositoryFactory factory) {
        this.application = application;
        this.repository = repository;
        this.repoFactory = factory;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(NotificationViewModel.class)) {
            return (T) new NotificationViewModel(repoFactory);
        } else if (modelClass.isAssignableFrom(TeacherViewModel.class)) {
            return (T) new TeacherViewModel(repoFactory);
        } else if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(repoFactory);
        } else if (modelClass.isAssignableFrom(EventViewModel.class)) {
            return (T) new EventViewModel(repoFactory);
        } else if (modelClass.isAssignableFrom(SessionViewModel.class)) {
            return (T) new SessionViewModel(repoFactory);
        } else if (modelClass.isAssignableFrom(DashboardViewModel.class)) {
            return (T) new DashboardViewModel(repository);
        } else if (modelClass.isAssignableFrom(GradeViewModel.class)) {
            return (T) new GradeViewModel(repoFactory);
        } else if (modelClass.isAssignableFrom(LocalDataViewModel.class)) {
            return (T) new LocalDataViewModel(repository);
        } else if (modelClass.isAssignableFrom(SubjectViewModel.class)) {
            return (T) new SubjectViewModel(repository);
        } else if (modelClass.isAssignableFrom(ChapterViewModel.class)) {
            return (T) new ChapterViewModel(repository);
        } else if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(repoFactory);
        } else if (modelClass.isAssignableFrom(SchoolScheduleViewModel.class)) {
            return (T) new SchoolScheduleViewModel(repository);
        } else if (modelClass.isAssignableFrom(BusRouteViewModel.class)) {
            return (T) new BusRouteViewModel(repository);
        } else if (modelClass.isAssignableFrom(DressCodeViewModel.class)) {
            return (T) new DressCodeViewModel(repository);
        } else if (modelClass.isAssignableFrom(GalleryViewModel.class)) {
            return (T) new GalleryViewModel(repository);
        } else if (modelClass.isAssignableFrom(SchoolDetailsViewModel.class)) {
            return (T) new SchoolDetailsViewModel(repoFactory);
        } else if (modelClass.isAssignableFrom(BookViewModel.class)) {
            return (T) new BookViewModel(repository);
        }  else if (modelClass.isAssignableFrom(StudentViewModel.class)) {
            return (T) new StudentViewModel(repoFactory);
        } else if (modelClass.isAssignableFrom(ContentFeedViewModel.class)) {
            return (T) new ContentFeedViewModel(repoFactory);
        } else if (modelClass.isAssignableFrom(VideoViewModel.class)) {
            return (T) new VideoViewModel(repoFactory);
        } else if (modelClass.isAssignableFrom(RoutineViewModel.class)) {
            return (T) new RoutineViewModel(repository);
        } else if (modelClass.isAssignableFrom(VideoAlbumViewModel.class)) {
            return (T) new VideoAlbumViewModel(repository);
        } else if (modelClass.isAssignableFrom(SuggestionViewModel.class)) {
            return (T) new SuggestionViewModel(repository);
        } else if (modelClass.isAssignableFrom(AttendanceViewModel.class)) {
            return (T) new AttendanceViewModel(repoFactory);
        } else if (modelClass.isAssignableFrom(LeaveApplicationViewModel.class)) {
            return (T) new LeaveApplicationViewModel(repository);
        }else if (modelClass.isAssignableFrom(ChatViewModel.class)) {
            return (T) new ChatViewModel(repoFactory);
        } else if (modelClass.isAssignableFrom(AssignmentViewModel.class)) {
            return (T) new AssignmentViewModel(repoFactory);
        }else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
