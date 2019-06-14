package com.example.adora;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPainter {

    GoogleMap mMap;

    public MapPainter(GoogleMap map) {
        mMap = map;
    }

    public void updateMarker (LatLng mSafeZone){
            Log.i("Painter", "Safe: " + mSafeZone);
            mMap.addMarker(new MarkerOptions().position(mSafeZone)
                    .title("Safe Zone"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mSafeZone));
        }
    }
