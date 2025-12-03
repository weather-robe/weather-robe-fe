package com.cookandroid.weatherrobe.Home;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cookandroid.weatherrobe.Home.dto.HomeResDTO;
import com.cookandroid.weatherrobe.Home.model.SharedWeatherViewModel;

import com.cookandroid.weatherrobe.R;
import com.cookandroid.weatherrobe.RetrofitClient;
import com.cookandroid.weatherrobe.common.CommonApiResponse;
import com.cookandroid.weatherrobe.common.CommonUtils;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeCodyKeywordFragment extends Fragment {

    private SharedWeatherViewModel sharedViewModel;

    private View dataLayoutWrapper;
    private ShimmerFrameLayout shimmerContainer;

    private TextView tvWeatherText;
    private LinearLayout llKeywordLayout;

    private int lastLoadedWeatherId = 0;

    private double currentLat = 37.5665;
    private double currentLon = 126.9780;
    private boolean isDataLoaded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_cody_keyword, container, false);

        View shimmerLayoutWrapper = view.findViewById(R.id.simmer_layout_wrapper);
        shimmerContainer = shimmerLayoutWrapper.findViewById(R.id.shimmer_container);

        dataLayoutWrapper = view.findViewById(R.id.data_layout_wrapper);
        tvWeatherText = dataLayoutWrapper.findViewById(R.id.tv_weather_description);
        llKeywordLayout = dataLayoutWrapper.findViewById(R.id.ll_keyword_tags);

        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedWeatherViewModel.class);

        startShimmerLoading();

        sharedViewModel.getWeatherId().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer weatherId) {
                if (weatherId != null && weatherId > 0) {
                    lastLoadedWeatherId = weatherId;
                    startShimmerLoading();
                    fetchWeatherKeywords(1, weatherId);
                }
            }
        });

        return view;
    }

    // -------------------------
    // ⭐ HomeFragment가 호출하는 함수
    // -------------------------
    public void updateLocation(double lat, double lon) {
        this.currentLat = lat;
        this.currentLon = lon;

        // 좌표는 실제로 keyword API 호출에 직접 쓰이지 않음 (weatherId 기반)
        // weatherId 업데이트는 SharedViewModel에서 온다.
    }

    private void startShimmerLoading() {
        dataLayoutWrapper.setVisibility(View.GONE);
        shimmerContainer.setVisibility(View.VISIBLE);
        shimmerContainer.startShimmer();
    }

    private void stopShimmerLoading() {
        shimmerContainer.stopShimmer();
        shimmerContainer.setVisibility(View.GONE);
        dataLayoutWrapper.setVisibility(View.VISIBLE);
    }

    private void fetchWeatherKeywords(int userId, int weatherId) {

        HomeApi api = RetrofitClient.getClient("https://api.weather-robe.kro.kr/")
                .create(HomeApi.class);

        api.getWeatherKeywords(userId, weatherId)
                .enqueue(new Callback<CommonApiResponse<HomeResDTO.PostKeywordDTO>>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonApiResponse<HomeResDTO.PostKeywordDTO>> call,
                                           @NonNull Response<CommonApiResponse<HomeResDTO.PostKeywordDTO>> res) {

                        stopShimmerLoading();

                        if (res.isSuccessful() && res.body() != null && res.body().getSuccess() != null) {
                            HomeResDTO.Weather weatherData = res.body().getSuccess().getWeather();
                            updateKeywordUI(weatherData);
                        } else {
                            updateKeywordUI(null);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CommonApiResponse<HomeResDTO.PostKeywordDTO>> call, @NonNull Throwable t) {
                        stopShimmerLoading();
                        updateKeywordUI(null);
                    }
                });
    }

    private void updateKeywordUI(HomeResDTO.Weather weatherData) {
        if (!isAdded()) return;

        if (weatherData == null) {
            tvWeatherText.setText("날씨 정보를 불러오는 데 실패했습니다.");
            llKeywordLayout.removeAllViews();
            return;
        }

        tvWeatherText.setText(weatherData.getText());

        List<String> keywords = weatherData.getKeywords();
        llKeywordLayout.removeAllViews();

        if (keywords != null) {
            for (String keyword : keywords) {
                TextView tagView = createKeywordTag(keyword);
                llKeywordLayout.addView(tagView);
            }
        }
    }

    private TextView createKeywordTag(String keyword) {
        Context context = getContext();
        TextView textView = new TextView(context);
        textView.setText(CommonUtils.addKeywordIcon(keyword));

        textView.setBackgroundResource(R.drawable.label_bg);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, dpToPx(6), 0);
        textView.setLayoutParams(params);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        return textView;
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}
