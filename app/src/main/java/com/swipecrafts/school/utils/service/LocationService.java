package com.swipecrafts.school.utils.service;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.repository.LocationRepository;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.school.ui.driver.live.BusLocationFragment;
import com.swipecrafts.school.utils.Utils;

import javax.inject.Inject;


/**
 * Created by Madhusudan Sapkota on 7/23/2018.
 */
public class LocationService extends Service {

    public static final String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    // to broadcast info out of the Service i.e. Activity/Fragment
    public static final String LOCATION_SERVICE_STOPPED_FILTER = "com.swipecrafts.schoolapp.service.LOCATION_SERVICE_STOPED";
    public static final String LOCATION_SERVICE_ERROR_FILTER = "com.swipecrafts.schoolapp.service.LOCATION_SERVICE_ERROR";
    public static final String LOCATION_SERVICE_UPDATES_FILTER = "com.swipecrafts.schoolapp.service.LOCATION_SERVICE_UPDATES";
    public static final String LOCATION_SERVICE_NAVIGATE_FILTER = "com.swipecrafts.schoolapp.service.LOCATION_SERVICE_NAVIGATE";

    public static final String LOCATION_SERVICE_NAVIGATE_ARG = "com.swipecrafts.schoolapp.service.LOCATION_SERVICE_NAVIGATE_KEY";
    public static final String LOCATION_SERVICE_LAT_ARG = "com.swipecrafts.schoolapp.service.LOCATION_SERVICE_LAT_KEY";
    public static final String LOCATION_SERVICE_LNG_ARG = "com.swipecrafts.schoolapp.service.LOCATION_SERVICE_LNG_KEY";
    public static final String LOCATION_SERVICE_MESSAGE_ARG = "com.swipecrafts.schoolapp.service.LOCATION_SERVICE_MESSAGE_KEY";

    // to listen info from the Activity/Fragment
    public static final String LOCATION_SERVICE_STOP_FILTER = "com.swipecrafts.schoolapp.service.LOCATION_SERVICE_STOP";
    public static final String LOCATION_SERVICE_RE_INIT_FILTER = "com.swipecrafts.schoolapp.service.LOCATION_SERVICE_RE_INIT";

    public static final String SOURCE_RECEIVER_FILTER = "com.swipecrafts.schoolapp.service.Location";
    public static final String LOCATION_SERVICE_BEARING_ARG = "bearing";
    public static final String LOCATION_SERVICE_ALTITUDE_ARG = "altitude";

    private static final long INTERVAL = 1000 * 2;
    private static final long FASTEST_INTERVAL = 1000;
    private static final int LIVE_BUS_NOTIFICATION_ID = 101;
    private static final String NOTIFICATION_CHANNEL_ID = "Live_Notification_Channel";

    private final IBinder mBinder = new LocalBinder();

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;

    private Location mCurrentLocation;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    private boolean firstTime = true;
    private boolean activeNotification;

    private LocationManager mLocationManager;

    @Inject
    RepositoryFactory repositoryFactory;
    private LocationRepository mLiveBusRespository;

    private final BroadcastReceiver actionServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = ""+intent.getAction();
            Log.e("LocationService", "Service Action: "+ action);
            switch (action) {
                case LOCATION_SERVICE_STOP_FILTER:
                    stopLocationUpdates();
                    destroyNotification();

                    // sending Broadcast saying LocationService has been Stopped
                    Intent localIntent = new Intent(LOCATION_SERVICE_STOPPED_FILTER);
                    LocalBroadcastManager.getInstance(LocationService.this).sendBroadcast(localIntent);

                    stopSelf();
                    break;

                case LOCATION_SERVICE_RE_INIT_FILTER:
                    initLocationRequest();
                    break;
            }
        }
    };

    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            LocationService.this.mCurrentLocation = locationResult.getLocations().get(0);
            Log.e("LocationService: ", " Location Lat: " + mCurrentLocation.getLatitude() + " Lng: " + mCurrentLocation.getLongitude());
            synchronized (LocationService.this){
                // sending Broadcast with current Location Details
                Intent localIntent = new Intent(LOCATION_SERVICE_UPDATES_FILTER)
                        .putExtra(LOCATION_SERVICE_LAT_ARG, mCurrentLocation.getLatitude())
                        .putExtra(LOCATION_SERVICE_LNG_ARG, mCurrentLocation.getLongitude());
                LocalBroadcastManager.getInstance(LocationService.this).sendBroadcast(localIntent);
                mLiveBusRespository.postLocation(mCurrentLocation);
                createNotification(mCurrentLocation);
            }
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            boolean isLocation = locationAvailability.isLocationAvailable();
            Log.e("LocationService", "onLocationAvailability: " + isLocation);
        }
    };

    private void createLocationRequest() {
        Log.e("LocationService", "createLocationRequest");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(Utils.getLocationMode(this));
    }

    private void initLocationRequest() {
        Log.e("LocationService", "initLocationRequest");
        if (mLocationRequest == null){
           createLocationRequest();
        }
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest mLocationSettingsRequest = builder.build();
        Task<LocationSettingsResponse> locationResponse = mSettingsClient.checkLocationSettings(mLocationSettingsRequest);
        locationResponse.addOnSuccessListener(locationSettingsResponse -> {
            Log.e("LocationService", "Location Setting Success: Successful acquisition of location information!!");
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        });
        locationResponse.addOnFailureListener(e -> {
            int statusCode = ((ApiException) e).getStatusCode();
            String message;
            switch (statusCode) {
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    message = "Resolution Required";
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    message = "Setting change unavailable";
                    break;
                default:
                    message = "N/A";
                    break;
            }

            Log.e("LocationService", "Location Setting Error: "+ message);
            // sending Broadcast saying LocationService has a problem getting current Location
            Intent localIntent = new Intent(LOCATION_SERVICE_ERROR_FILTER).putExtra(LOCATION_SERVICE_MESSAGE_ARG, message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        });
    }

    private void createNotification(Location location) {
        if (firstTime) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, new Intent(LOCATION_SERVICE_STOP_FILTER), PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intent = new Intent(LOCATION_SERVICE_NAVIGATE_FILTER);
            intent.putExtra(LOCATION_SERVICE_NAVIGATE_ARG, BusLocationFragment.class.getSimpleName());
            PendingIntent noticeIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);


            mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            mBuilder.setSmallIcon(R.drawable.school_splash_icon)
                    .setContentTitle("Broadcasting Bus Location")
                    .setOnlyAlertOnce(true)
                    .setShowWhen(true)
                    .setOngoing(true)
                    .setContentIntent(noticeIntent)
                    .addAction(R.drawable.ic_stop, "Stop", pIntent);
            firstTime = false;
            activeNotification = true;
        }

        String updates = "Current Location: Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude();
        mBuilder.setContentText(updates);
        notificationManager.notify(LIVE_BUS_NOTIFICATION_ID, mBuilder.build());
    }

    private void stopLocationUpdates() {
        if (mFusedLocationClient != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void destroyNotification() {
        Log.e("LocationService", "destroyNotification");
        if (activeNotification) {
            notificationManager.cancel(LIVE_BUS_NOTIFICATION_ID);
            activeNotification = false;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("LocationService", "onCreate");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);


        ((SchoolApplication) getApplication()).applicationComponent().inject(this);
        mLiveBusRespository = repositoryFactory.create(LocationRepository.class);

        registerReceiver(actionServiceReceiver, new IntentFilter(LOCATION_SERVICE_RE_INIT_FILTER));
        registerReceiver(actionServiceReceiver, new IntentFilter(LOCATION_SERVICE_STOP_FILTER));

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initLocationRequest();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("LocationService", "OnBind");
        return null;
    }



    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("LocationService", "onUnbind");
        stopLocationUpdates();
        destroyNotification();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e("LocationService", "onDestroy");
        unregisterReceiver(actionServiceReceiver);
        super.onDestroy();
    }

    public class LocalBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

}
