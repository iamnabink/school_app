package com.swipecrafts.school.ui.dashboard.settings;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.local.prefs.AppPreferencesRepository;
import com.swipecrafts.school.data.model.api.UserType;
import com.swipecrafts.school.data.model.others.AppSetting;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.login.ChangePasswordDFragment;

import javax.inject.Inject;

public class SettingFragment extends BaseFragment {


    @Inject
    AppPreferencesRepository prefRepository;
    AppSetting setting;
    private Toolbar toolbar;

    private LinearLayout changePassword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.settingToolBar);

        changePassword = (LinearLayout) view.findViewById(R.id.changePasswordContainer);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        setting = prefRepository.getSettings();

        if (setting.isUserLoggedIn() && setting.getUserType() != UserType.TEACHER) changePassword.setVisibility(View.VISIBLE);
        else changePassword.setVisibility(View.GONE);

        changePassword.setOnClickListener(v -> {
            ChangePasswordDFragment changePasswordDFragment = new ChangePasswordDFragment();
            changePasswordDFragment.show(getFragmentManager(), "Change Password");
        });

    }

}
