package com.cookandroid.weatherrobe.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity; // AppCompatActivity Import

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class AppLocationManager {

    private static final String TAG = "AppLocationManager";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private LocationUpdateListener listener;
    private Context context;
    private AppCompatActivity activity;

    private Location lastSentLocation;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATE = 100.0f;

    public AppLocationManager(AppCompatActivity activity, LocationUpdateListener listener) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.listener = listener;
        initLocationClient();
    }

    private void initLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 5000L)
                .setMinUpdateIntervalMillis(2000L)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    if (shouldSendLocationUpdate(location)) {
                        listener.onLocationReceived(location.getLatitude(), location.getLongitude());
                        lastSentLocation = location;
                    }
                }
            }
        };
    }

    private boolean shouldSendLocationUpdate(Location newLocation) {
        if (lastSentLocation == null) {
            return true;
        }
        float distance = lastSentLocation.distanceTo(newLocation);
        return distance > MIN_DISTANCE_CHANGE_FOR_UPDATE;
    }

    public void requestPermissionsAndStartUpdates() {
        if (checkPermissions()) {
            startLocationUpdates();
        } else {
            activity.requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
            Log.d(TAG, "Location permission requested.");
        }
    }

    public boolean handlePermissionResult(int requestCode, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                listener.onPermissionDenied();
            }
            return true;
        }
        return false;
    }


    public void startLocationUpdates() {
        if (!checkPermissions()) {
            listener.onLocationFailed("Permission required to start updates.");
            return;
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
            );
            Log.d(TAG, "Location updates started.");
        } catch (SecurityException e) {
            listener.onLocationFailed("SecurityException: " + e.getMessage());
        }
    }

    public void stopLocationUpdates() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
            Log.d(TAG, "Location updates stopped.");
        }
    }

    public boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}