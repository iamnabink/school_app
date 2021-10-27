package com.swipecrafts.school.ui;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.db.User;
import com.swipecrafts.library.youtube.YTPlayerFragmentManager;
import com.swipecrafts.school.ui.base.BaseActivity;
import com.swipecrafts.school.ui.calendar.CalendarFragment;
import com.swipecrafts.school.ui.dashboard.DashboardFragment;
import com.swipecrafts.school.ui.dc.DigitalClassFragment;
import com.swipecrafts.school.ui.dc.dcfeed.ContentFeedFragment;
import com.swipecrafts.school.ui.home.HomeFragment;
import com.swipecrafts.school.ui.notification.NoticeListFragment;
import com.swipecrafts.school.ui.notification.NotificationFragment;
import com.swipecrafts.school.viewmodel.UserViewModel;

import java.lang.reflect.Field;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    public static final String digitalLoginTag = "DigitalLogin";
    private final String TAG = "mainActivity";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private UserViewModel userViewModel;

    private HomeFragment homeFragment = new HomeFragment();
    private NotificationFragment notificationFragment = new NotificationFragment();
    private DigitalClassFragment digitalClassFragment = new DigitalClassFragment();
    private DashboardFragment dashboardFragment = new DashboardFragment();
    private CalendarFragment calendarFragment = new CalendarFragment();

    private BottomNavigationView navigation;
    private User activeUser;

    private BottomNavigationView.OnNavigationItemSelectedListener navItemSelectedListener = item -> {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);

        switch (item.getItemId()) {
            case R.id.navigation_calendar:
                if (!(currentFragment instanceof CalendarFragment)) {
                    clearBackStackFragments();
                    replaceStateLossFragment(R.id.main_container, calendarFragment, CalendarFragment.class.getSimpleName());
                }
                return true;

            case R.id.navigation_notifications:
                if (!(currentFragment instanceof NoticeListFragment)) {
                    clearBackStackFragments();
                    replaceStateLossFragment(R.id.main_container, notificationFragment, NotificationFragment.class.getSimpleName());
                }
                return true;

            case R.id.navigation_home:
                if (!(currentFragment instanceof HomeFragment)) {
                    clearBackStackFragments();
                    replaceStateLossFragment(R.id.main_container, homeFragment, HomeFragment.class.getSimpleName());
                }
                return true;

            case R.id.navigation_digital_class:
                if (!(currentFragment instanceof DigitalClassFragment)) {
                    clearBackStackFragments();
                    replaceStateLossFragment(R.id.main_container, digitalClassFragment, digitalClassFragment.getClass().getSimpleName());
                }
                return true;

            case R.id.navigation_dashboard:
                if (!(currentFragment instanceof DashboardFragment)) {
                    clearBackStackFragments();
                    replaceStateLossFragment(R.id.main_container, dashboardFragment, DashboardFragment.class.getSimpleName());
                }
                return true;

        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ((SchoolApplication) getApplication()).applicationComponent().inject(this);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.getActiveUser().observe(this, user -> this.activeUser = user);

        findView();
        init();

        if (savedInstanceState == null){
            navigation.setSelectedItemId(R.id.navigation_home);
        }
    }

    private void findView() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
    }

    private void init() {
        disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(navItemSelectedListener);
    }

    @SuppressLint("RestrictedApi")
    public void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());

                // remove label from bottom navigation
//                removeBottomNavTextLabel(item);
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

    private void removeBottomNavTextLabel(BottomNavigationItemView item) {

        if (item == null) return;
        ViewGroup viewGroup = (ViewGroup) item;
        int padding = 0;

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                padding = v.getHeight();
                viewGroup.removeViewAt(i);
            }
        }

        Log.e("padding", padding + "");
        padding = 50;
        viewGroup.setPadding(item.getPaddingLeft(), (viewGroup.getPaddingTop() + padding) / 2, item.getPaddingRight(), item.getPaddingBottom());
    }

    public void setBottomNavigationVisible(boolean isVisible) {
        if (navigation != null)
            this.navigation.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void clickBottomNavigation(int id) {
        navigation.setSelectedItemId(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void clearBackStackFragments() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        if (fragment instanceof ContentFeedFragment) {
            if (((YTPlayerFragmentManager) fragment).closeYoutubePlayer()) {
                Log.e("Youtube Video", "No backing!!");
                return;
            }
            Log.e("Youtube Video", "backing!!");
        }

//        if (fragment instanceof LoginActivity) {
//            setBottomNavigationVisible(true);
//
//            String navigateTo = ((LoginActivity) fragment).getNavigateTo();
//
//            if (navigateTo == null) {
//
//            } else if (navigateTo.equalsIgnoreCase(DigitalClassFragment.class.getSimpleName())) {
//                navigation.setSelectedItemId(R.id.navigation_digital_class);
//                return;
//            } else if (navigateTo.equalsIgnoreCase(DashboardFragment.class.getSimpleName())) {
//                navigation.setSelectedItemId(R.id.navigation_dashboard);
//                return;
//            } else if (navigateTo.equalsIgnoreCase(HomeFragment.class.getSimpleName())) {
//                navigation.setSelectedItemId(R.id.navigation_home);
//                return;
//            } else if (navigateTo.equalsIgnoreCase(NotificationFragment.class.getSimpleName())) {
//                navigation.setSelectedItemId(R.id.navigation_notifications);
//                return;
//            }
//        }
        super.onBackPressed();
    }
}
