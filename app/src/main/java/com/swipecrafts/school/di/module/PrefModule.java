package com.swipecrafts.school.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.remote.ApiHeader;
import com.swipecrafts.school.utils.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Madhusudan Sapkota on 2/23/2018.
 */

@Module
public class PrefModule {

    private static final String PREF_KEY_API_KEY = "PREF_KEY_API_KEY";
    private String mApiKey;
    private final SharedPreferences mPrefs;

    @Inject
    public PrefModule(Application mApplication, String apiKey) {
        this.mPrefs = mApplication.getSharedPreferences(Constants.APP_PREF_NAME, Context.MODE_PRIVATE);
        this.mApiKey = apiKey;
        this.mPrefs.edit().putString(PREF_KEY_API_KEY, apiKey).apply();
    }

    @Provides
    @Singleton
    AppPreferencesRepository provideAppPrefRepository(){
        return new AppPreferencesRepository(mPrefs);
    }

    @Provides
    @Singleton
    ApiHeader provideApiHeader(AppPreferencesRepository mPrefRepository, DataManager.LoggedInMode loggedInMode){
        Log.e("loggedType", mPrefRepository.getLoggedMode()+"");
       return new ApiHeader(mPrefRepository.getPublicApiHeader(), mPrefRepository.getSchoolId(), mPrefRepository.getProtectedApiHeader(), loggedInMode);
    }

    @Provides
    DataManager.LoggedInMode provideLoggedMode(AppPreferencesRepository mPrefRepository){
        return mPrefRepository.getLoggedMode() == 0 ? DataManager.LoggedInMode.LOGGED_OUT_MODE : DataManager.LoggedInMode.LOGGED_IN_MODE;
    }

}
