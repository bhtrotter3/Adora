package com.example.adora;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.TimeUnit;

public class GPSTracker extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback {
    GoogleMap mMap;
    //MapPainter mPainter = new MapPainter();

    FusedLocationProviderClient mFusedLocationClient;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    SafeZoneDecay mDecay = new SafeZoneDecay();
    // ...
    LocationCallback mLocationCallback;
    LatLng mSafeZoneStart = new LatLng(0,0);
    LatLng mSafeZoneCompare = new LatLng(0,0);
    LatLng mSafeZone = new LatLng(0,0);
    //boolean mapReady = false;

    static final int MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    Log.i("GPSTracker", "Location: " + location.getLatitude() + " " + location.getLongitude());
                    mSafeZoneCompare = new LatLng(location.getLatitude(), location.getLongitude());
                   // mPainter.updateMarker(mSafeZone);

                }

            }

        };

        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationUpdates();

        //...............
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null && mFusedLocationClient != null) {
            requestLocationUpdates();
        } else {
            buildGoogleApiClient();
            requestLocationUpdates();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
            requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //
    }

    public void requestLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval FIX!! 120000
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_FINE_LOCATION);

            // MY_PERMISSION_REQUEST_READ_FINE_LOCATION is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }
        else{
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,null);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        if (mSafeZone != mSafeZoneCompare && mSafeZoneCompare != mSafeZoneStart){
            mMap.addMarker(new MarkerOptions().position(mSafeZoneCompare)
                    .title("Safe Zone"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mSafeZoneCompare));
            mSafeZone = mSafeZoneCompare;
        }
    }
}