package com.cookandroid.weatherrobe.Home.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedWeatherViewModel extends ViewModel {
    private final MutableLiveData<Integer> selectedWeatherId = new MutableLiveData<>();
    private final MutableLiveData<String> selectedWeatherFeedback = new MutableLiveData<>();

    private final MutableLiveData<Double> currentLatitude = new MutableLiveData<>(37.5665);
    private final MutableLiveData<Double> currentLongitude = new MutableLiveData<>(126.9780);

    public LiveData<Integer> getWeatherId() {
        return selectedWeatherId;
    }

    public LiveData<String> getWeatherFeedback() {
        return selectedWeatherFeedback;
    }

    public LiveData<Double> getCurrentLatitude() {
        return currentLatitude;
    }

    public LiveData<Double> getCurrentLongitude() {
        return currentLongitude;
    }

    public void setWeatherId(int id) {
        selectedWeatherId.setValue(id);
    }

    public void setWeatherFeedback(String feedback) {
        selectedWeatherFeedback.setValue(feedback);
    }

    public void setLocation(double latitude, double longitude) {
        if (currentLatitude.getValue() == null || !currentLatitude.getValue().equals(latitude)) {
            currentLatitude.setValue(latitude);
        }
        if (currentLongitude.getValue() == null || !currentLongitude.getValue().equals(longitude)) {
            currentLongitude.setValue(longitude);
        }
    }
}