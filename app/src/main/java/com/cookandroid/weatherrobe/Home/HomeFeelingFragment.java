package com.cookandroid.weatherrobe.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookandroid.weatherrobe.Home.dto.HomeReqDTO;
import com.cookandroid.weatherrobe.Home.dto.HomeResDTO;
import com.cookandroid.weatherrobe.Home.model.SharedWeatherViewModel;
import com.cookandroid.weatherrobe.R;
import com.cookandroid.weatherrobe.RetrofitClient;
import com.cookandroid.weatherrobe.common.CommonApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFeelingFragment extends Fragment {

    private SharedWeatherViewModel sharedViewModel;

    TextView tvModerate, tvCold, tvHot;
    SharedPreferences prefs;

    private double currentLat = 37.5665;
    private double currentLon = 126.9780;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home_feeling, container, false);

        prefs = requireContext().getSharedPreferences("weather_pref", Context.MODE_PRIVATE);

        tvModerate = root.findViewById(R.id.tvModerate);
        tvCold = root.findViewById(R.id.tvCold);
        tvHot = root.findViewById(R.id.tvHot);

        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedWeatherViewModel.class);

        return root;
    }

    public void updateLocation(double lat, double lon) {
        this.currentLat = lat;
        this.currentLon = lon;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListeners();

        sharedViewModel.getWeatherFeedback().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String feedback) {
                updateFeedbackUI(feedback);
            }
        });
    }

    private void setListeners() {
        tvModerate.setOnClickListener(v -> {
            Integer weatherId = sharedViewModel.getWeatherId().getValue();
            if (weatherId == null || weatherId <= 0) return;

            String currentFeedback = sharedViewModel.getWeatherFeedback().getValue();
            String newFeedback = "적당".equals(currentFeedback) ? "" : "적당";
            fetchWeatherFeedback(1, weatherId, newFeedback);
        });

        tvCold.setOnClickListener(v -> {
            Integer weatherId = sharedViewModel.getWeatherId().getValue();
            if (weatherId == null || weatherId <= 0) return;

            String currentFeedback = sharedViewModel.getWeatherFeedback().getValue();
            String newFeedback = "추움".equals(currentFeedback) ? "" : "추움";
            fetchWeatherFeedback(1, weatherId, newFeedback);
        });

        tvHot.setOnClickListener(v -> {
            Integer weatherId = sharedViewModel.getWeatherId().getValue();
            if (weatherId == null || weatherId <= 0) return;

            String currentFeedback = sharedViewModel.getWeatherFeedback().getValue();
            String newFeedback = "더움".equals(currentFeedback) ? "" : "더움";
            fetchWeatherFeedback(1, weatherId, newFeedback);
        });
    }

    private void resetBackground() {
        tvModerate.setBackgroundResource(R.drawable.label_bg);
        tvCold.setBackgroundResource(R.drawable.label_bg);
        tvHot.setBackgroundResource(R.drawable.label_bg);
    }

    private void updateFeedbackUI(String feedback) {
        resetBackground();

        if (feedback == null || feedback.isEmpty()) return;

        switch(feedback) {
            case "적당":
                tvModerate.setBackgroundResource(R.drawable.label_bg_yellow);
                break;
            case "추움":
                tvCold.setBackgroundResource(R.drawable.label_bg_sky_blue);
                break;
            case "더움":
                tvHot.setBackgroundResource(R.drawable.label_bg_red);
                break;
        }
    }

    private void fetchWeatherFeedback(int userId, int weatherId, String feedback) {

        HomeReqDTO reqDto = new HomeReqDTO();
        HomeReqDTO.PostHomeDTO postDto = reqDto.new PostHomeDTO(feedback);

        HomeApi apiService = RetrofitClient.getClient("https://api.weather-robe.kro.kr/")
                .create(HomeApi.class);

        apiService.postFeedback(userId, weatherId, postDto)
                .enqueue(new Callback<CommonApiResponse<HomeResDTO.PostFeedbackDTO>>() {
                    @Override
                    public void onResponse(Call<CommonApiResponse<HomeResDTO.PostFeedbackDTO>> call,
                                           Response<CommonApiResponse<HomeResDTO.PostFeedbackDTO>> res) {

                        if (!isAdded()) return;

                        if (res.isSuccessful() && res.body() != null
                                && res.body().getSuccess() != null) {

                            HomeResDTO.Daily weatherData = res.body().getSuccess().getDaily();

                            String finalFeedback = weatherData != null
                                    ? weatherData.getFeedback()
                                    : "";

                            sharedViewModel.setWeatherFeedback(finalFeedback);

                        }
                    }

                    @Override
                    public void onFailure(Call<CommonApiResponse<HomeResDTO.PostFeedbackDTO>> call, Throwable t) {

                    }
                });
    }
}
