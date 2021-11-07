package com.android.print.demo.util;

import android.content.Context;
import android.location.LocationManager;

public class LocationHandler {
    public static final boolean isLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        boolean networkProvider = locationManager.isProviderEnabled("network");
        boolean gpsProvider = locationManager.isProviderEnabled("gps");
        if (networkProvider || gpsProvider) {
            return true;
        }
        return false;
    }
}
