package com.swipecrafts.school.ui.driver;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.ui.base.BaseActivity;
import com.swipecrafts.school.viewmodel.UserViewModel;

import javax.inject.Inject;


public class DriverDashboard extends BaseActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private UserViewModel userViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);

        ((SchoolApplication) getApplication()).applicationComponent().inject(this);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);


        replaceStateLossFragment(R.id.driver_fragment_container, new DriverDashboardFragment(), DriverDashboardFragment.class.getSimpleName());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
