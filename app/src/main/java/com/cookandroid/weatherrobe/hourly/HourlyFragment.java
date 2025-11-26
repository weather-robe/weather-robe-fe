package com.cookandroid.weatherrobe.hourly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cookandroid.weatherrobe.R;

public class HourlyFragment extends Fragment {

    public HourlyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_hourly, container, false);
    }
}
