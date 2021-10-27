package com.swipecrafts.school;

import android.app.Application;

import com.swipecrafts.school.di.component.ApplicationComponent;
import com.swipecrafts.school.di.component.DaggerApplicationComponent;
import com.swipecrafts.school.di.module.ApplicationModule;
import com.swipecrafts.school.di.module.NetModule;
import com.swipecrafts.school.di.module.PrefModule;
import com.swipecrafts.school.di.module.RoomModule;
import com.swipecrafts.school.utils.TypefaceUtil;

/**
 * Created by Madhusudan Sapkota on 2/11/2018.
 */

public class SchoolApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .roomModule(new RoomModule(this))
                .netModule(new NetModule(this, BuildConfig.API_ENDPOINT))
                .prefModule(new PrefModule(this, BuildConfig.API_KEY))
                .build();

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/roboto-regular.ttf");
    }

    public ApplicationComponent applicationComponent() {
        return mApplicationComponent;
    }
}
