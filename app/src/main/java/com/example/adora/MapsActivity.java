

        package com.example.adora;

        import android.Manifest;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.os.Bundle;
        import android.os.Looper;
        import android.support.annotation.NonNull;
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
        //import com.google.android.gms.maps.MapFragment;
        //import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener//,
        //OnMapReadyCallback
         {

    public static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mlocation;

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                mlocation = location;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null && mFusedLocationClient != null) {
            requestLocationUpdates();
        } else {
            buildGoogleApiClient();
            //setUpMapIfNeeded();
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
        handleNewLocation(mlocation);
    }

    public void requestLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //??
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
        //??
    }
/*
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            MapFragment mapFrag = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFrag.getMapAsync(this);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
    */
/*
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("You are here."));
    }
*/
    public void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

}
/*
    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }
} */