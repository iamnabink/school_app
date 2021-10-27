package com.swipecrafts.school.utils.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.repository.LocationRepository;
import com.swipecrafts.school.data.repository.RepositoryFactory;
import com.swipecrafts.school.ui.dashboard.livebus.model.LiveLocationData;

import java.util.Calendar;

import javax.inject.Inject;

/**
 * Created by Madhusudan Sapkota on 11/12/2018.
 */
public class LocationTracker extends Service {
    public static String SCHOOL_ID = "SCHOOL_ID";
    public static String DRIVER_ID = "DRIVER_ID";
    public static String BUS_ID = "BUS_ID";
    public static String BUS_NAME = "BUS_NAME";
    public static String BUS_NUMBER = "BUS_NUMBER";


    private static final long INTERVAL = 1000 * 2;
    private static final long FASTEST_INTERVAL = 1000;
    private final String TAG = this.getClass().getSimpleName();
    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            if (locationProviderClient != null && locationCallBack != null){
                locationProviderClient.removeLocationUpdates(locationCallBack);
            }
            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };
    private String NOTIFICATION_CHANNEL_ID = "Live_Notification_Channel";

    @Inject
    RepositoryFactory repositoryFactory;
    private LocationRepository mLiveBusRespository;

    private Location location;
    private String schoolId;
    private long driverId;
    private long busID;

    private LiveLocationData liveLocationData = new LiveLocationData();
    private LocationCallback locationCallBack = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            final String path = getString(R.string.live_bus_map_path, schoolId, String.valueOf(driverId));
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
            location = locationResult.getLastLocation();

            liveLocationData.setSchoolId(schoolId);
            liveLocationData.setBusId(busID);
            liveLocationData.setDriverId(driverId);
            liveLocationData.setBusName(busName);
            liveLocationData.setBusNumber(busNumber);
            liveLocationData.setLatitude(location.getLatitude());
            liveLocationData.setLongitude(location.getLongitude());
            liveLocationData.setAltitude(location.getAltitude());
            liveLocationData.setAddedAt(Calendar.getInstance().getTimeInMillis());
            if (location != null) {
                Log.d(TAG, "location update " + location);
                ref.setValue(liveLocationData);
                Intent intent = new Intent(LocationService.LOCATION_SERVICE_UPDATES_FILTER);
                intent.putExtra(BUS_NAME, busName);
                intent.putExtra(BUS_NUMBER, busNumber);
                intent.putExtra(LocationService.LOCATION_SERVICE_LAT_ARG, location.getLatitude());
                intent.putExtra(LocationService.LOCATION_SERVICE_LNG_ARG, location.getLongitude());
                intent.putExtra(LocationService.LOCATION_SERVICE_BEARING_ARG, location.getBearing());
                intent.putExtra(LocationService.LOCATION_SERVICE_ALTITUDE_ARG, location.getAltitude());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
        }
    };
    private FusedLocationProviderClient locationProviderClient;
    private String busNumber;
    private String busName;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        schoolId = intent.getStringExtra(SCHOOL_ID);
        driverId = intent.getLongExtra(DRIVER_ID, 0);
        busID = intent.getLongExtra(BUS_ID, 0);
        busName = intent.getStringExtra(BUS_NAME);
        busNumber = intent.getStringExtra(BUS_NUMBER);
        liveLocationData.setDriverId(driverId);
        liveLocationData.setBusId(busID);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();


        ((SchoolApplication) getApplication()).applicationComponent().inject(this);
        mLiveBusRespository = repositoryFactory.create(LocationRepository.class);

        buildNotification();
        loginToFirebase();
    }

    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        String channelId = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel("101", NOTIFICATION_CHANNEL_ID);
        }
        // Create the persistent notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.school_splash_icon);
        startForeground(1, mBuilder.build());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName){
        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }

    private void loginToFirebase() {
        // Authenticate with Firebase, and request location updates
        String email = getString(R.string.firebase_email);
        String password = getString(R.string.firebase_password);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "firebase auth success");
                        requestLocationUpdates();
                    } else {
                        Log.d(TAG, "firebase auth failed");
                    }
                });
    }

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(INTERVAL);
        request.setFastestInterval(FASTEST_INTERVAL);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            locationProviderClient.requestLocationUpdates(request, locationCallBack, Looper.myLooper());
        }else {
            Log.e(TAG, "No Permission ");
        }
    }
}
