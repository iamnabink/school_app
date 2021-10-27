package com.swipecrafts.school.ui.notification;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.api.UserType;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.viewmodel.NotificationViewModel;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseFragment {

    private final String TAG = "NotificationFragment";
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private Toolbar toolbar;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private NotificationViewModel notificationViewModel;
    private UserType userType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication())
                .applicationComponent()
                .inject(this);

        //Set up and subscribe (observe) to the ViewModel
        notificationViewModel = ViewModelProviders.of(this, viewModelFactory).get(NotificationViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_notification, container, false);

        findView(mainView);
        init();

        return mainView;
    }

    private void findView(View mainView) {
        toolbar = (Toolbar) mainView.findViewById(R.id.notificationToolbar);


        userType = notificationViewModel.getCurrentUserType();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), userType);
        mViewPager = (ViewPager) mainView.findViewById(R.id.notification_type_container);
        tabLayout = (TabLayout) mainView.findViewById(R.id.notification_tabs);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        if (userType == UserType.TEACHER){
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        }else {
           tabLayout.setVisibility(View.GONE);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final UserType userType;

        SectionsPagerAdapter(FragmentManager fm, UserType userType) {
            super(fm);
            this.userType = userType;
        }

        @Override
        public Fragment getItem(int position) {
            UserType type =  position == 0 ? UserType.PARENT : UserType.TEACHER;
            return NoticeListFragment.newInstance(type);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return userType == UserType.TEACHER ? 2 : 1;
        }
    }
}
