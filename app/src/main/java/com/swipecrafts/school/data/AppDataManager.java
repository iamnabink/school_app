package com.swipecrafts.school.data;

import android.content.Context;
import android.net.NetworkInfo;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.file.AppFileManager;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.remote.ApiHeader;
import com.swipecrafts.school.data.remote.AppApiRepository;
import com.swipecrafts.school.utils.Utils;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by amitshekhar on 07/07/17.
 */
@Singleton
public class AppDataManager implements DataManager {

    private final Context mContext;

    private final AppApiRepository mApiHelper;

    private final AppDbRepository mDbHelper;

    private final AppPreferencesRepository mPreferencesHelper;

    private final AppFileManager mAppFileManager;

    @Inject
    public AppDataManager(Context mContext, AppApiRepository mApiHelper, AppDbRepository mDbHelper, AppPreferencesRepository mPreferencesHelper, AppFileManager appFileManager) {
        this.mApiHelper = mApiHelper;
        this.mContext = mContext;
        this.mDbHelper = mDbHelper;
        this.mPreferencesHelper = mPreferencesHelper;
        this.mAppFileManager = appFileManager;
    }

    @Override
    public AppApiRepository getApiRepo() {
        return mApiHelper;
    }

    @Override
    public AppDbRepository getDbRepo() {
        return mDbHelper;
    }

    @Override
    public AppPreferencesRepository getPrefRepo() {
        return mPreferencesHelper;
    }

    @Override
    public AppFileManager getAppFileManager() {
        return mAppFileManager;
    }

    @Override
    public boolean isNetworkConntected() {
        return Utils.isOnline(mContext);
    }

    @Override
    public NetworkInfo getNetworkType() {
        return Utils.getNetworkType(mContext);
    }

    @Override
    public String getSchoolId() {
        return getPrefRepo().getSchoolId();
    }

    @Override
    public long getUserId() {
        return getApiHeader().getProtectedApiHeader().getSESUId();
    }

    public ApiHeader getApiHeader() {
        return mApiHelper.getApiHeader();
    }
}
