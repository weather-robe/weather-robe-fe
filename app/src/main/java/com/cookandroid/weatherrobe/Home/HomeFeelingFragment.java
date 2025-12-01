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
            final Integer weatherIdObj = sharedViewModel.getWeatherId().getValue();
            if (weatherIdObj == null || weatherIdObj <= 0) {
                Log.e("HomeFeelingFragment", "필수 ID 값(Weather ID)이 유효하지 않습니다. API 호출 취소.");
                return;
            }
            int weatherId = weatherIdObj;
            String currentFeedback = sharedViewModel.getWeatherFeedback().getValue();
            String newFeedback = "적당".equals(currentFeedback) ? "" : "적당";
            fetchWeatherFeedback(1, weatherId, newFeedback);
        });

        tvCold.setOnClickListener(v -> {
            final Integer weatherIdObj = sharedViewModel.getWeatherId().getValue();
            if (weatherIdObj == null || weatherIdObj <= 0) {
                Log.e("HomeFeelingFragment", "필수 ID 값(Weather ID)이 유효하지 않습니다. API 호출 취소.");
                return;
            }
            int weatherId = weatherIdObj;
            String currentFeedback = sharedViewModel.getWeatherFeedback().getValue();
            String newFeedback = "추움".equals(currentFeedback) ? "" : "추움";
            fetchWeatherFeedback(1, weatherId, newFeedback);
        });

        tvHot.setOnClickListener(v -> {
            final Integer weatherIdObj = sharedViewModel.getWeatherId().getValue();
            if (weatherIdObj == null || weatherIdObj <= 0) {
                Log.e("HomeFeelingFragment", "필수 ID 값(Weather ID)이 유효하지 않습니다. API 호출 취소.");
                return;
            }
            int weatherId = weatherIdObj;
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
        if (feedback == null || feedback.isEmpty()) {
            resetBackground();
            return;
        }

        switch(feedback) {
            case "적당":
                resetBackground();
                tvModerate.setBackgroundResource(R.drawable.label_bg_yellow);
                break;
            case "추움":
                resetBackground();
                tvCold.setBackgroundResource(R.drawable.label_bg_sky_blue);
                break;
            case "더움":
                resetBackground();
                tvHot.setBackgroundResource(R.drawable.label_bg_red);
                break;
            default:
                resetBackground();
                break;
        }
    }

    private void fetchWeatherFeedback(int userId, int weatherId, String feedback) {
        HomeReqDTO reqDto = new HomeReqDTO();
        HomeReqDTO.PostHomeDTO postDto = reqDto.new PostHomeDTO(feedback);

        HomeApi apiService = RetrofitClient.getClient("https://api.weather-robe.kro.kr/")
                .create(HomeApi.class);

        Call<CommonApiResponse<HomeResDTO.PostFeedbackDTO>> call =
                apiService.postFeedback(userId, weatherId, postDto);

        call.enqueue(new Callback<CommonApiResponse<HomeResDTO.PostFeedbackDTO>>() {
            @Override
            public void onResponse(Call<CommonApiResponse<HomeResDTO.PostFeedbackDTO>> call,
                                   Response<CommonApiResponse<HomeResDTO.PostFeedbackDTO>> res) {

                if (!isAdded()) return;

                if (res.isSuccessful() && res.body() != null && res.body().getSuccess() != null) {

                    HomeResDTO.Daily weatherData = res.body().getSuccess().getDaily();

                    if (weatherData != null) {
                        Log.d("API_CALL", "피드백 업데이트 성공");

                        String finalFeedback = weatherData.getFeedback();

                        if (finalFeedback == null) {
                            finalFeedback = "";
                        }

                        sharedViewModel.setWeatherFeedback(finalFeedback);
                    } else {
                        Log.e("API_CALL", "응답은 성공했으나 Daily 데이터가 누락됨.");
                    }


                } else {
                    Log.e("API_CALL", "응답 실패. Status: " + res.code() + ", Message: " + (res.body() != null ? res.body().getError() : "N/A"));
                }
            }

            @Override
            public void onFailure(Call<CommonApiResponse<HomeResDTO.PostFeedbackDTO>> call, Throwable t) {
                if (!isAdded()) return;

                Log.e("CodyAPI", "키워드 요청 실패: " + t.getMessage());
            }
        });
    }
}