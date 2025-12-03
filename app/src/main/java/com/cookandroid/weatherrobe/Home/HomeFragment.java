package com.cookandroid.weatherrobe.Home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cookandroid.weatherrobe.R;

public class HomeFragment extends Fragment {

    private double currentLat = 37.5665;
    private double currentLon = 126.9780;
    private boolean isDataLoaded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        loadChildFragments();

        if (!isDataLoaded) {
            notifyChildFragments(currentLat, currentLon);
        }

        return view;
    }

    public void updateLocation(double latitude, double longitude) {
        if (isDataLoaded && currentLat == latitude && currentLon == longitude) {
            return;
        }
        this.currentLat = latitude;
        this.currentLon = longitude;
        this.isDataLoaded = true;

        if (isAdded()) {
            Log.d("HomeFragment", "위치 수신. 자식 프래그먼트 업데이트 시작.");
            notifyChildFragments(latitude, longitude);
        }
    }

    private void loadChildFragments() {
        FragmentManager fm = getChildFragmentManager();

        fm.beginTransaction()
                .replace(R.id.container_home_weather, new HomeWeatherFragment())
                .replace(R.id.container_home_cody_keyword, new HomeCodyKeywordFragment())
                .replace(R.id.container_home_feeling, new HomeFeelingFragment())
                .replace(R.id.container_home_cody_recommend, new HomeCodyRecommendFragment())
                .commit();
    }

    private void notifyChildFragments(double latitude, double longitude) {
        HomeWeatherFragment weatherFragment = (HomeWeatherFragment) getChildFragmentManager().findFragmentById(R.id.container_home_weather);

        if (weatherFragment != null) {
        }
    }
}