package com.swipecrafts.school.ui.driver.live;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.manager.Status;
import com.swipecrafts.school.data.model.db.BusDriver;
import com.swipecrafts.library.views.ErrorView;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.utils.service.LocationService;
import com.swipecrafts.school.utils.service.LocationTracker;
import com.swipecrafts.school.viewmodel.UserViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static android.content.Context.LOCATION_SERVICE;


public class BusLocationFragment extends BaseFragment implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION = 100;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    Geocoder geocoder;

    // filters to get Info from LocationService
    private IntentFilter filter1 = new IntentFilter(LocationService.LOCATION_SERVICE_STOP_FILTER);
    private IntentFilter filter2 = new IntentFilter(LocationService.LOCATION_SERVICE_ERROR_FILTER);
    private IntentFilter filter3 = new IntentFilter(LocationService.LOCATION_SERVICE_UPDATES_FILTER);
    private LocationService mService;
    private boolean mStatus;
    private long mStartTime;
    private ErrorView errorView;
    private TextView stationLocation;
    private LocationManager locationManager;
    private long driverId = 0;
    private String schoolId = "";
    private long busId = 0;
    private Switch driverStatus;
    private TextView busNumber;
    private LatLng mLastKnownLocation;
    private UserViewModel userViewModel;

    private List<BusDriver> driverData = new ArrayList<>();
    private Observer<Resource<List<BusDriver>>> driverDataObserver = response -> {
        if (response == null || response.status == Status.LOADING) return;

        if (response.status == Status.SUCCESS) {
            driverData = response.data;
        }
    };
    private GoogleMap mGoogleMap;
    private MapView mapView;
    private Toolbar toolbar;
    private Marker mapMarker;
    // Receives the Info from LocationService
    private BroadcastReceiver busLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = "" + intent.getAction();
            Log.e("LocationService", "Received in Fragment: " + action);
            switch (action) {
                case LocationService.LOCATION_SERVICE_UPDATES_FILTER:
                    mStatus = !mStatus || mStatus;
                    String bnum = intent.getStringExtra(LocationTracker.BUS_NUMBER);
                    String bnm = intent.getStringExtra(LocationTracker.BUS_NAME);

                    busNumber.setText(String.format("%s (%s)", bnm, bnum));
                    driverStatus.setChecked(true);
                    driverStatus.setText("Online");

                    double lat = intent.getDoubleExtra(LocationService.LOCATION_SERVICE_LAT_ARG, 0);
                    double lng = intent.getDoubleExtra(LocationService.LOCATION_SERVICE_LNG_ARG, 0);
                    float bearing = intent.getFloatExtra(LocationService.LOCATION_SERVICE_BEARING_ARG, 0);
                    String updates = "Latitude: " + lat + "\n Longitude: " + lng;

                    try {
                        List<Address> addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                        String city = addresses.get(0).getLocality();
//                        String state = addresses.get(0).getAdminArea();
//                        String country = addresses.get(0).getCountryName();
//                        String postalCode = addresses.get(0).getPostalCode();
//                        String knownName = addresses.get(0).getFeatureName();
                        stationLocation.setText(address);
                    } catch (IOException e) {
                        e.printStackTrace();
                        stationLocation.setText("loading location...");
                    }
                    Log.e("LocationService", "Location: " + updates);
                    updateMap(lat, lng, bearing);
                    break;
                case LocationService.LOCATION_SERVICE_ERROR_FILTER:
                    driverStatus.setEnabled(false);
                    driverStatus.setText("Offline");
                    String message = "" + intent.getStringExtra(LocationService.LOCATION_SERVICE_MESSAGE_ARG);
                    String errorMessage = Utils.isOnline(getContext()) ? "An error occurred while trying to broadcast location." : Constants.NO_INTERNET_MESSAGE;
                    errorView.setViewType(ErrorView.ERROR_WITH_BUTTON)
                            .setErrorTitle(errorMessage)
                            .setButtonText("Retry")
                            .setOnRetryListener(v -> {
                                if (Utils.isOnline(getContext())) {
                                    if (checkLocationPermission()) {
                                        errorView.setVisibility(View.GONE);
                                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(LocationService.LOCATION_SERVICE_RE_INIT_FILTER));
                                    } else {
                                        errorView.setErrorTitle("You have not enabled the Location Service.").apply();
                                    }
                                } else {
                                    errorView.setErrorTitle(Constants.NO_INTERNET_MESSAGE).apply();
                                }
                            }).apply();
                    errorView.setVisibility(View.VISIBLE);

                    Log.e("LocationService", "Error: " + message);
                    break;
            }
        }
    };
    private BusDriver selectedBusDetails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
    }

    private void updateMap(double lat, double lng, float bearing) {
        LatLng newLocation = new LatLng(lat, lng);
        if (mGoogleMap != null) {
            float[] distance = new float[1];
            if (mLastKnownLocation == null) {
                distance[0] = 5001;
            } else {
                Location.distanceBetween(
                        mLastKnownLocation.latitude, mLastKnownLocation.longitude,
                        lat, lng, distance
                );
            }
            if (mapMarker == null) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_side_view));
                markerOptions.title("Me");
                markerOptions.rotation(bearing);
                markerOptions.position(newLocation);
                mapMarker = mGoogleMap.addMarker(markerOptions);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 14));
            } else {
                mapMarker.setPosition(newLocation);
                if (distance[0] > 300)
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 14));
            }
            this.mLastKnownLocation = newLocation;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_bus_broadcast, container, false);

        findView(view);
        mapView.onCreate(savedInstanceState);
        init();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.getBusDetails().observe(this, driverDataObserver);
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.toolbar);

        errorView = (ErrorView) view.findViewById(R.id.errorLayout);

        busNumber = (TextView) view.findViewById(R.id.busNumberTV);
        driverStatus = (Switch) view.findViewById(R.id.driverStatusSwitch);

        mapView = view.findViewById(R.id.mapView);
        stationLocation = (TextView) view.findViewById(R.id.stationName);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        geocoder = new Geocoder(getContext(), Locale.getDefault());
        mapView.getMapAsync(this);
        driverStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                bindService();
                stationLocation.setText("Loading location..");
            } else {
                unbindService();
                stationLocation.setText("Not available");
                busNumber.setText("Not available");
                driverStatus.setChecked(false);
                driverStatus.setText("Offline");
                Toast.makeText(getContext(), "Live bus navigation has been ended.", Toast.LENGTH_SHORT).show();
            }
        });

        errorView.setOnRetryListener(v -> {
            if (checkLocationService()) initLocationService();
            else errorView.setViewType(ErrorView.ERROR_WITH_BUTTON).apply();
        });

        Log.e("LiveBus", "Init Checking LocationService");
        if (!mStatus && checkLocationService()) {
            initLocationService();
        }
    }

    private void initLocationService() {
        Log.e("LiveBus", "Initiating Location Service");
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) return;
        if (!mStatus) {
//            bindService();
            if (checkLocationPermission()) {
                if (mGoogleMap != null){
                    mGoogleMap.setMyLocationEnabled(true);
                    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null)
                    updateMap(location.getLatitude(), location.getLongitude(), location.getBearing());
            }
        }
        // start UI update LiveBroadCasting is Active now.
        errorView.setVisibility(View.GONE);
        mapView.setVisibility(View.VISIBLE);
    }

    private boolean checkLocationService() {
        boolean isPermissionAvailable = checkLocationPermission();
        boolean isServiceAvailable = true;// checkGPS();

        if (isPermissionAvailable && isServiceAvailable) return true;
        Log.e("LiveBus", "Permission: " + isPermissionAvailable + " GPS Service: " + isServiceAvailable);
        showAlertToUser(!isPermissionAvailable);
        return false;
    }

    private boolean checkLocationPermission() {
        Log.e("LiveBus", "Checking Location Permission");
        return ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkGPS() {
        Log.e("LiveBus", "Checking GPS Service");
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showAlertToUser(boolean isPermission) {
        Log.e("LiveBus", "Showing Alert Message. Is Permission request: " + isPermission);

        String permissionText = "To use this Application Location permission needs to be provided.";
        String serviceText = "To use this Application GPS service needs to be enabled. Enable GPS Of your device.";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(isPermission ? permissionText : serviceText)
                .setCancelable(false)
                .setPositiveButton(
                        "OK",
                        (dialog, id) -> {
                            if (isPermission) {
                                requestPermissions(LocationService.permissions, REQUEST_LOCATION);
                            } else {
                                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, id) -> {
            driverStatus.setEnabled(false);
            driverStatus.setText("Offline");
            errorView.setViewType(ErrorView.ERROR_WITH_BUTTON).apply();
            errorView.setVisibility(View.VISIBLE);
            dialog.cancel();
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void bindService() {
        if (mStatus) return;

        Intent intent = new Intent(getActivity(), LocationTracker.class);

        if (driverData != null && !driverData.isEmpty()) {
            // Message Dialog Initialization
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
            View chooseBusView = layoutInflaterAndroid.inflate(R.layout.choose_bus_alert_view, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(chooseBusView);
            builder.setCancelable(true);
            AlertDialog dialog = builder.create();

            RadioGroup radioGroup = chooseBusView.findViewById(R.id.busRadioGroup);
            Button chooseBtn = chooseBusView.findViewById(R.id.chooseButton);

            for (BusDriver bd : driverData) {
                RadioButton rbtn = new RadioButton(getContext());
                rbtn.setId(View.NO_ID);
                rbtn.setTag(bd);
                rbtn.setText(String.format("%s (%s)", bd.getBusName(), bd.getBusNo()));
                radioGroup.addView(rbtn);
            }

            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton rbtn = radioGroup.findViewById(checkedId);
                if (rbtn != null) {
                    busNumber.setText(rbtn.getText());
                }
            });

            chooseBtn.setOnClickListener(view -> {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getContext(), "Please select least one bus to start live navigation.", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton rbtn = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                selectedBusDetails = (BusDriver) rbtn.getTag();
                driverStatus.setChecked(true);
                driverStatus.setText("Online");


                driverId = userViewModel.getUserId();
                schoolId = userViewModel.getSchoolId();
                intent.putExtra(LocationTracker.SCHOOL_ID, schoolId);
                intent.putExtra(LocationTracker.DRIVER_ID, driverId);
                intent.putExtra(LocationTracker.BUS_ID, selectedBusDetails.getBusId());
                intent.putExtra(LocationTracker.BUS_NAME, selectedBusDetails.getBusName());
                intent.putExtra(LocationTracker.BUS_NUMBER, selectedBusDetails.getBusNo());
                getActivity().startService(intent);
                mStatus = true;
                mStartTime = System.currentTimeMillis();
                Toast.makeText(getContext(), "Live bus navigation has started.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            });
            dialog.setOnDismissListener(dialog1 -> {
                if (!mStatus) driverStatus.setChecked(false);
            });
            dialog.show();
        } else {
            driverStatus.setChecked(true);
            driverStatus.setText("Online");

            driverId = userViewModel.getUserId();
            schoolId = userViewModel.getSchoolId();
            intent.putExtra(LocationTracker.SCHOOL_ID, schoolId);
            intent.putExtra(LocationTracker.DRIVER_ID, driverId);
            intent.putExtra(LocationTracker.BUS_ID, "N/A");
            intent.putExtra(LocationTracker.BUS_NAME, "N/A");
            intent.putExtra(LocationTracker.BUS_NUMBER, "N/A");
            getActivity().startService(intent);
            mStatus = true;
            mStartTime = System.currentTimeMillis();
            Toast.makeText(getContext(), "Live bus navigation has started.", Toast.LENGTH_SHORT).show();

            //driverStatus.setChecked(false);
            //driverStatus.setText("Offline");
            //Toast.makeText(getContext(), "You can not go live because you are not associated with any bus!", Toast.LENGTH_SHORT).show();
        }
    }

    private void unbindService() {
        if (!mStatus) return;
        String stop = "stop";
        getActivity().sendBroadcast(new Intent(stop));
        mStatus = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        if (mapView != null) mapView.onResume();
        super.onResume();

//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(busLocationReceiver, filter1);
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(busLocationReceiver, filter2);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(busLocationReceiver, filter3);
        if (!mStatus && checkLocationPermission() && checkGPS()) initLocationService();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(busLocationReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0) {
                    if (!mStatus && checkGPS()) initLocationService();
                    else showAlertToUser(false);
                } else {
                    driverStatus.setEnabled(false);
                    driverStatus.setText("Offline");
                    errorView.setViewType(ErrorView.ERROR_WITH_BUTTON).apply();
                    errorView.setVisibility(View.VISIBLE);
                }
            }
        }
    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.driver_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_logout:
//                //  teacher/parent/driver can log out from system
//                CustomDialog dialog = showProgressDialog(null, "logout...");
//                userViewModel.logOutUser().observe(this, response -> {
//                    if (response == null) return;
//
//                    if (response.status == Status.SUCCESS) {
//                        dialog.dismissWithAnimation();
//                        startActivity(new Intent(getContext(), MainActivity.class));
//                        getActivity().finish();
//                    } else if (response.status == Status.ERROR) {
//                        String errorMessage = (response.code() == 1001 ? Utils.isOnline(getContext()) ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : response.message);
//                        showErrorDialog(dialog, getString(R.string.error_dialog_title), errorMessage, getString(R.string.dialog_ok), null);
//                    }
//                });
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
