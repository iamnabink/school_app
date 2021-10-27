package com.swipecrafts.school.ui.dashboard.busroute.map;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.db.Bus;
import com.swipecrafts.school.data.model.db.BusRoute;
import com.swipecrafts.school.viewmodel.BusRouteViewModel;

import java.util.List;

import javax.inject.Inject;

public class BusRouteMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String BUS_ID_KEY = "BUS_ID_KEY";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private GoogleMap mMap;
    private long busID = -1;
    private Bus bus;
    private BusRouteViewModel busRouteViewModel;

    private List<BusRoute> busRouteList;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        toolbar = findViewById(R.id.mapToolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ((SchoolApplication) getApplication()).applicationComponent().inject(this);

        busRouteViewModel = ViewModelProviders.of(this, viewModelFactory).get(BusRouteViewModel.class);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                busID = extras.getLong(BUS_ID_KEY);
            }
        }

        Log.e("bus", busID+"");

        if (busID != -1) {
            busRouteViewModel.getBusRoutes(busID).observe(this, (busRoutes -> {
                busRouteList = busRoutes;
                Log.e("routes", busRoutes.size()+"");
                setUpMarker();
            }));

            busRouteViewModel.getBus(busID).observe(this, (bus -> {
                if (bus == null) return;
                this.bus = bus;
                setTitle(bus.getBusName());
            }));
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        setUpMarker();
    }

    private void setUpMarker(){
        if (mMap == null || busRouteList == null) return;

        LatLng startPosition = null;
        for (BusRoute route: busRouteList){
            LatLng latLng = new LatLng(route.getLatitude(), route.getLongitude());

            if (startPosition == null)startPosition = latLng;

            MarkerOptions marker = new MarkerOptions();
            marker.position(latLng).title(route.getStationName());
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(marker).showInfoWindow();
        }

//        Log.e("map", "building");
//
//        ArrayList<LatLng> points = new ArrayList<>();
//        PolylineOptions lineOptions = new PolylineOptions();
//
//        LatLng startLatLng = null;
//        LatLng endLatLng = null;
//
//        for (int i = 0; i < busRouteList.size(); i++){
//            LatLng position = new LatLng(busRouteList.get(i).getLatitude(), busRouteList.get(i).getLongitude());
//            points.add(position);
//
//
//            if (i == 0) startLatLng = position;
//            if (i == busRouteList.size()-1) endLatLng = position;
//        }
//
//        // Adding all the points in the route to LineOptions
//        lineOptions.addAll(points);
//        lineOptions.width(4);
//        lineOptions.color(Color.RED);
//
//        MarkerOptions startPosition = new MarkerOptions();
//        MarkerOptions endPosition = new MarkerOptions();
//        startPosition.position(startLatLng);
//        endPosition.position(endLatLng);
//
//        startPosition.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//        endPosition.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//
//
//        // Add new marker to the Google Map Android API V2
//        mMap.addMarker(startPosition);
//        mMap.addMarker(endPosition);
//
//        // Drawing polyline in the Google Map for the i-th route
//        mMap.addPolyline(lineOptions);

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(startPosition));


        // Showing the current location in Google Map
        CameraPosition camPos = new CameraPosition.Builder()
                .target(startPosition)
                .zoom(12)
                .build();
        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
        mMap.animateCamera(camUpd3);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
