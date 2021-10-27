package com.swipecrafts.school.di.module;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import com.swipecrafts.school.data.AppDataManager;
import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.db.SchoolAppDatabase;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.remote.AppApiRepository;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.viewmodel.ViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by R_KAY on 8/18/2017.
 */
@Module
public class RoomModule {

    private final SchoolAppDatabase database;

    public RoomModule(Application application) {
        this.database = Room.databaseBuilder(
                application,
                SchoolAppDatabase.class,
                Constants.APP_DATABASE_NAME
        ).fallbackToDestructiveMigration().build();
    }


    @Provides
    @Singleton
    SchoolAppDatabase provideListItemDatabase(Application application) {
        return database;
    }

    @Provides
    @Singleton
    RepositoryFactory provideRepositoryFactory(AppApiRepository webService, AppDbRepository localDbService, AppPreferencesRepository prefService){
        return new RepositoryFactory(webService, localDbService, prefService);
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(Application application, AppDataManager repository, RepositoryFactory factory) {
        return new ViewModelFactory(application, repository, factory);
    }

    @Provides
    @Singleton
    AppDbRepository provideAppDbRepository() {
        return new AppDbRepository(database);
    }
}
