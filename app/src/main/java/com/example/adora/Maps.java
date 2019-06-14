package com.example.adora;

import com.google.android.gms.maps.model.LatLng;

public class Maps {

    Tracker mTracker = new Tracker();
    Decay mDecay = new Decay();
    Paint mPaint = new Paint();
    LatLng safeZone = mTracker.getSafeZone();
    mPaint.safe(safeZone)
}
