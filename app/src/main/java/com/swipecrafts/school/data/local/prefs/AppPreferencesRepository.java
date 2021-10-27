package com.swipecrafts.school.data.local.prefs;

import android.content.SharedPreferences;
import android.util.Log;

import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.data.model.db.User;
import com.swipecrafts.school.data.model.others.AppSetting;
import com.swipecrafts.school.data.remote.ApiHeader;

import javax.inject.Inject;

/**
 * Created by Madhusudan Sapkota
 */

public class AppPreferencesRepository implements PreferencesRepository {

    private static final String PREF_KEY_SCHOOL_ID = "PREF_KEY_SCHOOL_ID";

    private static final String PREF_KEY_API_KEY = "PREF_KEY_API_KEY";


    private static final String PREF_KEY_SSH_KEY = "PREF_KEY_SSH_KEY";
    private static final String PREF_KEY_SES_KEY = "PREF_KEY_SES_KEY";
    private static final String PREF_KEY_SES_N_ID = "PREF_KEY_SES_N_ID";
    private static final String PREF_KEY_SES_USER_ID = "PREF_KEY_SES_USER_ID";


    private static final String PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE";
    private static final String PREF_KEY_SES_USER_TYPE = "PREF_KEY_SES_USER_TYPE";

    private final SharedPreferences mPrefs;


    @Inject
    public AppPreferencesRepository(SharedPreferences pref) {
        this.mPrefs = pref;
    }

    public ApiHeader.ProtectedApiHeader getProtectedApiHeader() {
        String apiKey = mPrefs.getString(PREF_KEY_API_KEY, null);
        String sshKey = mPrefs.getString(PREF_KEY_SSH_KEY, null);
        String sesKey = mPrefs.getString(PREF_KEY_SES_KEY, null);
        String sesNId = mPrefs.getString(PREF_KEY_SES_N_ID, null);
        long sesUId = mPrefs.getLong(PREF_KEY_SES_USER_ID, 0);

        return new ApiHeader.ProtectedApiHeader(apiKey, sshKey, sesKey, sesNId, sesUId);
    }

    public void setProtectedApiHeader(User activeUser) {
        if (activeUser == null) {
            cleanProtectedApiHeader();
            setLoggedMode(DataManager.LoggedInMode.LOGGED_OUT_MODE);
            return;
        }

        mPrefs.edit().putString(PREF_KEY_SSH_KEY, activeUser.getSshKey()).apply();
        mPrefs.edit().putString(PREF_KEY_SES_KEY, activeUser.getSessionKey()).apply();
        mPrefs.edit().putString(PREF_KEY_SES_N_ID, activeUser.getSessionID()).apply();
        mPrefs.edit().putLong(PREF_KEY_SES_USER_ID, activeUser.getSessionUId()).apply();
        mPrefs.edit().putInt(PREF_KEY_USER_LOGGED_IN_MODE, DataManager.LoggedInMode.LOGGED_IN_MODE.value()).apply();
        setUserType(activeUser.getUserType());
    }

    public ApiHeader.PublicApiHeader getPublicApiHeader() {
        String apiKey = mPrefs.getString(PREF_KEY_API_KEY, null);
        return new ApiHeader.PublicApiHeader(apiKey);
    }

    public void setPublicApiHeader(ApiHeader.PublicApiHeader apiHeader) {
        mPrefs.edit().putString(PREF_KEY_API_KEY, apiHeader.getApiKey()).apply();
    }

    public int getLoggedMode() {
        return mPrefs.getInt(PREF_KEY_USER_LOGGED_IN_MODE, DataManager.LoggedInMode.LOGGED_OUT_MODE.value());
    }

    public void setLoggedMode(DataManager.LoggedInMode loggedMode) {
        mPrefs.edit().putInt(PREF_KEY_USER_LOGGED_IN_MODE, loggedMode.value()).apply();
    }

    public String getUserType() {
        return mPrefs.getString(PREF_KEY_SES_USER_TYPE, "parent");
    }

    public void setUserType(String type) {
        mPrefs.edit().putString(PREF_KEY_SES_USER_TYPE, type).apply();
    }

    public String getSchoolId() {
        return mPrefs.getString(PREF_KEY_SCHOOL_ID, null);
    }

    public void setSchoolId(String schoolId) {
        mPrefs.edit().putString(PREF_KEY_SCHOOL_ID, schoolId).apply();
    }

    public void cleanProtectedApiHeader() {
        mPrefs.edit().putString(PREF_KEY_SSH_KEY, "").apply();
        mPrefs.edit().putString(PREF_KEY_SES_KEY, "").apply();
        mPrefs.edit().putString(PREF_KEY_SES_N_ID, "").apply();
        mPrefs.edit().putLong(PREF_KEY_SES_USER_ID, 0).apply();
        mPrefs.edit().putString(PREF_KEY_SES_USER_TYPE, "").apply();
    }

    public void setString(String key, String value) {
        mPrefs.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return mPrefs.getString(key, null);
    }

    public AppSetting getSettings() {
        AppSetting setting = new AppSetting();
        Log.e("LoggedMode", getLoggedMode()+"");
        setting.setUserLoggedIn(getLoggedMode() == DataManager.LoggedInMode.LOGGED_IN_MODE.value());
        setting.setUserType(getUserType());
        return setting;
    }

    public void setAppSettings(AppSetting settings){
    }
}
