package com.cookandroid.weatherrobe.Home;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cookandroid.weatherrobe.Home.HomeWeatherFragment;
import com.cookandroid.weatherrobe.Home.HomeCodyKeywordFragment;
import com.cookandroid.weatherrobe.Home.HomeFeelingFragment;
import com.cookandroid.weatherrobe.Home.HomeCodyRecommendFragment;
import com.cookandroid.weatherrobe.R;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        loadChildFragments();
        return view;
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
}
