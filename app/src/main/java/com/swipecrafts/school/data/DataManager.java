package com.swipecrafts.school.data;

import android.net.NetworkInfo;

import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.file.AppFileManager;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.remote.AppApiRepository;

/**
 * Created by Madhusudan Sapkota on 07/07/17.
 */

public interface DataManager {

    AppApiRepository getApiRepo();

    AppDbRepository getDbRepo();

    AppPreferencesRepository getPrefRepo();

    AppFileManager getAppFileManager();

    boolean isNetworkConntected();

    NetworkInfo getNetworkType();

    String getSchoolId();

    long getUserId();

    enum LoggedInMode {

        LOGGED_OUT_MODE(0),
        LOGGED_IN_MODE(1);

        private final int value;

        LoggedInMode(int type) {
            value = type;
        }

        public int value() {
            return value;
        }
    }
}
