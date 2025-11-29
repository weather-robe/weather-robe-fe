package com.cookandroid.weatherrobe.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookandroid.weatherrobe.R;

public class HomeFeelingFragment extends Fragment {

    TextView tvModerate, tvCold, tvHot;
    SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home_feeling, container, false);

        // 1. SharedPreferences 준비
        prefs = requireContext().getSharedPreferences("weather_pref", Context.MODE_PRIVATE);

        // 2. TextView 연결
        tvModerate = root.findViewById(R.id.tvModerate);
        tvCold = root.findViewById(R.id.tvCold);
        tvHot = root.findViewById(R.id.tvHot);

        // 3. 클릭 리스너 등록
        setListeners();

        return root;
    }

    private void setListeners() {
        tvModerate.setOnClickListener(v -> {
            resetBackground();
            tvModerate.setBackgroundResource(R.drawable.label_bg_yellow);
            saveFeeling("moderate");
        });

        tvCold.setOnClickListener(v -> {
            resetBackground();
            tvCold.setBackgroundResource(R.drawable.label_bg_sky_blue);
            saveFeeling("cold");
        });

        tvHot.setOnClickListener(v -> {
            resetBackground();
            tvHot.setBackgroundResource(R.drawable.label_bg_red);
            saveFeeling("hot");
        });
    }

    private void resetBackground() {
        tvModerate.setBackgroundResource(R.drawable.label_bg);
        tvCold.setBackgroundResource(R.drawable.label_bg);
        tvHot.setBackgroundResource(R.drawable.label_bg);
    }

    private void saveFeeling(String value) {
        prefs.edit().putString("tempFeeling", value).apply();
    }
}
