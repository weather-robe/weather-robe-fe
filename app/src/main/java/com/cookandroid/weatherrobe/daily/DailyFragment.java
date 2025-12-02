package com.cookandroid.weatherrobe.daily;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.weatherrobe.R;
import com.cookandroid.weatherrobe.common.CommonApiResponse;
import com.cookandroid.weatherrobe.daily.api.DailyService;
import com.cookandroid.weatherrobe.daily.dto.DailyReqDTO;
import com.cookandroid.weatherrobe.daily.dto.DailyResDTO;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DailyFragment extends Fragment{

    private static final String BASE_URL = "https://api.weather-robe.kro.kr/";
    private DailyService dailyService;
    private RecyclerView recyclerView;
    private DailyWeatherAdapter weatherAdapter;
    private final List<DailyWeatherData> dataList = new ArrayList<>();
    private final DecimalFormat tempFormat = new DecimalFormat("0°");
    private double currentLat = 37.5665;
    private double currentLon = 126.9780;
    private boolean isDataLoaded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initRetrofit();

        return inflater.inflate(R.layout.fragment_daily, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view_weather);

        weatherAdapter = new DailyWeatherAdapter(dataList, true);
        recyclerView.setAdapter(weatherAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        if (!isDataLoaded) {
            fetchDailyWeather(1, currentLat, currentLon);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    public void updateLocation(double latitude, double longitude) {
        if (isDataLoaded && currentLat == latitude && currentLon == longitude) {
            return;
        }
        this.currentLat = latitude;
        this.currentLon = longitude;
        this.isDataLoaded = false;

        if (isAdded()) {
            fetchDailyWeather(1, currentLat, currentLon);
        }
    }

    private void initRetrofit() {
        if (getContext() == null) return;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dailyService = retrofit.create(DailyService.class);
    }

    private void fetchDailyWeather(int userId, double latitude, double longitude) {
        DailyReqDTO reqDto = new DailyReqDTO();
        DailyReqDTO.PostDailyDTO postDto = reqDto.new PostDailyDTO(latitude, longitude);

        dailyService.getWeatherForecast(userId, postDto).enqueue(new Callback<CommonApiResponse<DailyResDTO.PostDailyDTO>>() {
            @Override
            public void onResponse(@NonNull Call<CommonApiResponse<DailyResDTO.PostDailyDTO>> call,
                                   @NonNull Response<CommonApiResponse<DailyResDTO.PostDailyDTO>> response) {

                if (!isAdded() || response.body() == null) return;

                CommonApiResponse<DailyResDTO.PostDailyDTO> apiResponse = response.body();

                if (response.isSuccessful() && apiResponse.isSuccessful()) {
                    Log.d("API_CALL", "일자별 날씨 로드 성공");
                    isDataLoaded = true; // 성공적으로 로드 완료

                    DailyResDTO.PostDailyDTO successData = apiResponse.getSuccess();
                    updateWeatherList(successData);

                } else {
                    Log.e("API_CALL", "응답 오류: " + response.code() + ", " + apiResponse.getError());
                    weatherAdapter.setShimmering(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonApiResponse<DailyResDTO.PostDailyDTO>> call,
                                  @NonNull Throwable t) {
                weatherAdapter.setShimmering(false);
                Log.e("API_CALL", "통신 실패: " + t.getMessage(), t);
            }
        });
    }

    private void updateWeatherList(DailyResDTO.PostDailyDTO successData) {
        dataList.clear();

        boolean hasYesterday = false;

        java.text.SimpleDateFormat dayOfWeekFormat =
                new java.text.SimpleDateFormat("EEE", java.util.Locale.KOREA); // 요일 만들기 (금, 토, 일 이런식)

        // 어제 데이터 추가 (없는 경우 아예 false 상태로 유지)
        DailyResDTO.Yesterday yesterdayData = successData.getYesterday();
        System.out.println(yesterdayData);
        if (yesterdayData != null && yesterdayData.getTemp() != null && yesterdayData.getWeatherId() > 0) {
            DailyWeatherData yesterdayItem = convertToDailyWeatherData(
                    "어제",
                    yesterdayData.getDate(),
                    null,
                    yesterdayData.getTemp().getMin(),
                    yesterdayData.getTemp().getMax(),
                    true
            );
            dataList.add(yesterdayItem);
            hasYesterday = true;
        }

        // 일자별은 리스트로 추가
        List<DailyResDTO.Daily> dailyList = successData.getDaily();

        if (dailyList != null) {

            for (int i = 0; i < dailyList.size(); i++) {
                DailyResDTO.Daily dailyData = dailyList.get(i);

                String dayLabel;

                if (i == 0) {
                    dayLabel = "오늘";
                } else {
                    dayLabel = dayOfWeekFormat.format(dailyData.getDate());
                }

                DailyWeatherData dailyItem = convertToDailyWeatherData(
                        dayLabel,
                        dailyData.getDate(),
                        dailyData.getIcon(),
                        dailyData.getTemp().getMin(),
                        dailyData.getTemp().getMax(),
                        false
                );
                dataList.add(dailyItem);
            }
        }
        weatherAdapter.setHasYesterday(hasYesterday);
        weatherAdapter.setShimmering(false);
    }

    private DailyWeatherData convertToDailyWeatherData(String dayLabel, Date date, String icon, double minTemp,
                                                       double maxTemp, boolean isYesterday) {
        String dateValue = isYesterday ? "어제 날짜" : "오늘 날짜";
        if (date != null) {
            dateValue = android.text.format.DateFormat.format("MM월  d일", date).toString();
        }
        int iconRes = getWeatherIconResource(icon);
        String tempMinStr = tempFormat.format(minTemp);
        String tempMaxStr = tempFormat.format(maxTemp);

        return new DailyWeatherData(dayLabel, dateValue, iconRes, tempMinStr, tempMaxStr);
    }

    private int getWeatherIconResource(String icon) {
        if (icon == null) return R.drawable.ic_weather_cloudy;

        switch (icon) {
            case "01d":
            case "01n":
                return R.drawable.ic_weather_sunny;
            case "02d":
            case "02n":
                return R.drawable.ic_weather_cloudy_day;
            case "03d":
            case "03n":
            case "04d":
            case "04n":
            case "13d":
            case "13n":
            case "50d":
            case "50n":
                return R.drawable.ic_weather_cloudy;
            case "09d":
            case "09n":
            case "10d":
            case "10n":
            case "11d":
            case "11n":
                return R.drawable.ic_weather_rainy;
        }
        return R.drawable.ic_weather_cloudy;
    }
}