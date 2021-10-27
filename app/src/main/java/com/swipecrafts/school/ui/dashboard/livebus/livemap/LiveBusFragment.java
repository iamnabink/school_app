package com.swipecrafts.school.ui.dashboard.livebus.livemap;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dashboard.livebus.model.LiveLocationData;
import com.swipecrafts.school.viewmodel.UserViewModel;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;


public class LiveBusFragment extends BaseFragment implements OnMapReadyCallback {

    public static final String ARG_DRIVER_ID = "com.swipecrafts.schoolapp.DRIVER_ID";
    public static final String ARG_BUS_ID = "com.swipecrafts.schoolapp.BUS_ID";
    private static final String ARG_BUS_NUMBER = "BUS_NUMBER";

    private static final String TAG = LiveBusFragment.class.getSimpleName();
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private UserViewModel userViewModel;
    private HashMap<String, Marker> mMarkers = new HashMap<>();

    private String mSchoolId = "0";
    private String mDriverId = "0";
    private String mBusId = "0";
    private String mBusNumber = "N/A";

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");

    private MapView mapView;
    private GoogleMap mGoogleMap;
    private String mLastUpdateTime;
    private Marker mapMarker;
    private LiveLocationData oldLiveData;
    private Toolbar toolbar;

    private TextView busNumberTV, currentLocationTV;

    Geocoder geocoder;


    public static LiveBusFragment newInstance(String busId, String driverId, String busNumber) {
        LiveBusFragment fragment = new LiveBusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BUS_ID, busId);
        args.putString(ARG_DRIVER_ID, driverId);
        args.putString(ARG_BUS_NUMBER, busNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.liveBusToolbar);

        mapView = view.findViewById(R.id.liveBusMap);

        busNumberTV = view.findViewById(R.id.busNumberTV);
        currentLocationTV = view.findViewById(R.id.currentLocationTV);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        mSchoolId = userViewModel.getSchoolId();

        geocoder = new Geocoder(getContext(), Locale.getDefault());

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this);

        busNumberTV.setText(mBusNumber);
    }

    private void addMarker(LiveLocationData locationData) {
        if (locationData == null) return;
        LatLng newLocation = new LatLng(locationData.getLatitude(), locationData.getLongitude());
        String time = simpleDateFormat.format(new Date(locationData.getAddedAt()));

        if (mapMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_side_view));
            markerOptions.title(locationData.getBusName() + " at:" + time);
            markerOptions.position(newLocation);
            mapMarker = mGoogleMap.addMarker(markerOptions);
        }else {
            mapMarker.setTitle(locationData.getBusName() + "  at:" + time);
            mapMarker.setPosition(newLocation);
            mapMarker.setRotation(locationData.getBearing());
        }

        float[] distance = new float[1];
        if(oldLiveData != null) {
            Location.distanceBetween(
                    locationData.getLatitude(), locationData.getLongitude(),
                    oldLiveData.getLatitude(), oldLiveData.getLongitude(),
                    distance
            );
        }else{
            distance[0] = 1001;
        }

        if (distance[0] > 1000) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 16));
        }

        oldLiveData = locationData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDriverId = getArguments().getString(ARG_DRIVER_ID);
            mBusId = getArguments().getString(ARG_BUS_ID);
            mBusNumber = getArguments().getString(ARG_BUS_NUMBER);
        }

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live_bus, container, false);

        findView(view);
        mapView.onCreate(savedInstanceState);
        init();

        return view;
    }

    private void loginToFirebase() {
        String email = getString(R.string.firebase_email);
        String password = getString(R.string.firebase_password);
        // Authenticate with Firebase and subscribe to updates
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                subscribeToUpdates();
                Log.d(TAG, "firebase auth success");
            } else {
                Log.d(TAG, "firebase auth failed");
            }
        });
    }

    private void subscribeToUpdates() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(getString(R.string.live_bus_map_path, mSchoolId, mDriverId));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LiveLocationData data = dataSnapshot.getValue(LiveLocationData.class);
                if(data != null) {
                    addMarker(data);
                    try {
                        List<Address> addresses = geocoder.getFromLocation(data.getLatitude(), data.getLongitude(), 1);
                        String address = addresses.get(0).getAddressLine(0);
                        currentLocationTV.setText(address);
                    } catch (IOException e) {
                        e.printStackTrace();
                        currentLocationTV.setText("N/A");
                    }
                }else{
                    currentLocationTV.setText("Bus has not gone online yet.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        this.mGoogleMap.setMaxZoomPreference(16);
        loginToFirebase();
    }

    @Override
    public void onResume() {
        if (mapView != null)  mapView.onResume();
        super.onResume();
    }
}
