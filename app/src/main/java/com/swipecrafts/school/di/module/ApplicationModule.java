/*
 * *
 *  * Copyright (C) 2017 Ryan Kay Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.swipecrafts.school.di.module;

import android.app.Application;

import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.AppDataManager;
import com.swipecrafts.school.data.local.db.AppDbRepository;
import com.swipecrafts.school.data.local.file.AppFileManager;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.remote.AppApiRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Madhusudan Sapkota
 */
@Module
public class ApplicationModule {
    private final SchoolApplication application;

    public ApplicationModule(SchoolApplication application) {
        this.application = application;
    }

    @Provides
    SchoolApplication provideRoomApplication() {
        return application;
    }

    @Provides
    @Singleton
    AppDataManager providesAppDataManager(AppApiRepository apiRepository, AppDbRepository dbRepository, AppPreferencesRepository preferencesRepository, AppFileManager appFileManager){
        return new AppDataManager(application, apiRepository, dbRepository, preferencesRepository, appFileManager);
    }

    @Provides
    @Singleton
    AppFileManager provideAppFileManager(){
        return new AppFileManager(application);
    }

    @Provides
    Application provideApplication() {
        return application;
    }
}
