package com.cookandroid.weatherrobe.Home.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedWeatherViewModel extends ViewModel {
    private final MutableLiveData<Integer> selectedWeatherId = new MutableLiveData<>();
    public LiveData<Integer> getWeatherId() {
        return selectedWeatherId;
    }

    public void setWeatherId(int id) {
        selectedWeatherId.setValue(id);
    }
}