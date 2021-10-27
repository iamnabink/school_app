package com.swipecrafts.school.ui.splash;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.manager.Status;
import com.swipecrafts.school.data.model.api.UserType;
import com.swipecrafts.school.ui.MainActivity;
import com.swipecrafts.school.ui.driver.DriverDashboard;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.LogUtils;
import com.swipecrafts.library.preetydialog.CustomDialog;
import com.swipecrafts.school.viewmodel.LocalDataViewModel;
import com.swipecrafts.school.viewmodel.SessionViewModel;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity {

    private final String TAG = "SplashActivity";
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private MutableLiveData<Integer> hasLocalDataToDB;
    private LocalDataViewModel localDataViewModel;
    private SessionViewModel sessionViewModel;

    private boolean isSchoolId = false;
    private String schoolId = null;
    private boolean isDataDownloaded = false;
    private boolean isRefreshing = false;

    private ImageView schoolIcon;
    private AnimationDrawable lightsAnimation;

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // checking for type intent filter
            if (Constants.REGISTRATION_COMPLETE.equals(intent.getAction())) {
                String schoolId = intent.getStringExtra("schoolId");
                // gcm successfully registered
                // now subscribe to `global` topic to receive app wide notifications
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.EVENT_NOTIFICATION);
                FirebaseMessaging.getInstance().subscribeToTopic(schoolId+Constants.GENERAL_NOTIFICATION);

            } else if (Constants.PUSH_NOTIFICATION.equals(intent.getAction())) {
                // new push notification is received
                String message = intent.getStringExtra("message");
                LogUtils.infoLog("Push Notification", message);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        schoolIcon = (ImageView) findViewById(R.id.schoolIcon);

        ((SchoolApplication) getApplication()).applicationComponent().inject(this);

        localDataViewModel = ViewModelProviders.of(this, viewModelFactory).get(LocalDataViewModel.class);
        sessionViewModel = ViewModelProviders.of(this, viewModelFactory).get(SessionViewModel.class);

        loadSchoolId();
    }

    private void loadSchoolId() {
        sessionViewModel.loadSchoolKey().observe(this, response -> {
            if (response == null || isSchoolId) return;

            if (response.status == Status.SUCCESS) {
                isSchoolId = true;
                schoolId = response.data;
                Log.e("NewCode", "School ID " + response.data);
                loadImportantData(response.data);
            } else if (response.status == Status.ERROR) {
                Log.e("NewCode", "School ID error " + response.data);
                showDialog(getString(R.string.download_error_title), getString(R.string.splash_error_message));
            } else {
                isRefreshing = true;
            }
        });
    }

    private void loadImportantData(String schoolId) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.APP_PREF_NAME, 0);
        String deviceToken = pref.getString("FCMRegId", FirebaseInstanceId.getInstance().getToken());

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", deviceToken);
        registrationComplete.putExtra("schoolId", schoolId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

        sessionViewModel.loadImportantDataFromRemote().observe(this, response -> {
            if (response == null || isDataDownloaded) return;

            if (response.status == Status.SUCCESS) {
                isDataDownloaded = true;
                Log.e("NewCode", "Data download Success" + response.data);
                startApplication();
            } else if (response.status == Status.ERROR) {
                Log.e("NewCode", "Data download error " + response.data);
                if (response.data != null && response.data.hasDashboardItems() && response.data.hasDigitalClass()) {
                    startApplication();
                } else {
                    showDialog(getString(R.string.download_error_title), getString(R.string.splash_error_message));
                }
            } else {
                Log.e("NewCode", "Splash download loading " + response.data);
                isRefreshing = true;
            }
        });
    }

    private void showDialog(String title, String message) {
        CustomDialog dialog = new CustomDialog(this, CustomDialog.ERROR_TYPE);
        dialog
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("RETRY")
                .setConfirmClickListener(widget -> {
                    widget.dismissWithAnimation();
                    if (isSchoolId) loadImportantData(schoolId);
                    else loadSchoolId();
                })
                .changeAlertType(CustomDialog.ERROR_TYPE);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void startApplication() {

       if (UserType.DRIVER.getValue().equalsIgnoreCase( sessionViewModel.loadUserType())){
           startActivity(new Intent(this, DriverDashboard.class));
           finish();
       }else{
           startActivity(new Intent(this, MainActivity.class));
           finish();
       }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Constants.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Constants.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
}
