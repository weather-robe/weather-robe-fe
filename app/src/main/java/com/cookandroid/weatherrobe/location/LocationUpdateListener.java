package com.cookandroid.weatherrobe.location;

public interface LocationUpdateListener {
    void onLocationReceived(double latitude, double longitude);
    void onPermissionDenied();
    void onLocationFailed(String error);
}