package com.cookandroid.weatherrobe.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cookandroid.weatherrobe.Home.model.Current;
import com.cookandroid.weatherrobe.Home.model.SharedWeatherViewModel;
import com.cookandroid.weatherrobe.Home.model.Today;
import com.cookandroid.weatherrobe.Home.model.Yesterday;
import com.cookandroid.weatherrobe.R;
import com.cookandroid.weatherrobe.RetrofitClient;
import com.cookandroid.weatherrobe.hourly.HourlyRequest;
import androidx.lifecycle.ViewModelProvider;
import com.google.gson.Gson;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeWeatherFragment extends Fragment {
    private SharedWeatherViewModel sharedViewModel;
    private ImageView ivWeather, ivYesterdayHigh, ivYesterdayLow;
    private TextView tvTemp, tvFeel, tvPop, tvPm;
    private TextView tvYesterdayHigh, tvYesterdayLow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_weather, container, false);

        initViews(view);
        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedWeatherViewModel.class);
        loadWeatherData();

        return view;
    }

    private void initViews(View v) {
        ivWeather = v.findViewById(R.id.iv_weather);

        tvTemp = v.findViewById(R.id.tv_temp);
        tvFeel = v.findViewById(R.id.tv_feel);
        tvPop = v.findViewById(R.id.tv_pop);
        tvPm = v.findViewById(R.id.tv_pm);

        tvYesterdayHigh = v.findViewById(R.id.tv_yesterday_high);
        tvYesterdayLow = v.findViewById(R.id.tv_yesterday_low);

        ivYesterdayHigh = v.findViewById(R.id.iv_yesterday_up);
        ivYesterdayLow = v.findViewById(R.id.iv_yesterday_down);
    }

    private void loadWeatherData() {
        HomeApi api = RetrofitClient.getClient("https://api.weather-robe.kro.kr/")
                .create(HomeApi.class);

        SharedPreferences prefs =
                requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        int userId = prefs.getInt("userId", -1);
        HomeRequest req = new HomeRequest(37.5, 127.0);

        api.getHomeWeather(userId, req).enqueue(new Callback<HomeWeatherResponse>() {
            @Override
            public void onResponse(Call<HomeWeatherResponse> call, Response<HomeWeatherResponse> res) {
                Log.d("HomeAPI", "Response success: " + res.isSuccessful());
                if (!res.isSuccessful() || res.body() == null){
                    Log.e("HomeAPI", "body null");
                    return;
                }
                Log.d("CHECK_JSON", new Gson().toJson(res.body()));
                Log.d("HomeAPI", "updateWeatherUI 실행됨");
                updateWeatherUI(res.body());
            }

            @Override
            public void onFailure(Call<HomeWeatherResponse> call, Throwable t) {
                Log.e("WeatherAPI", t.getMessage());
            }
        });
    }
    private void updateWeatherUI(HomeWeatherResponse res) {

        Current c = res.success.current;
        Today t = res.success.today;
        Yesterday y = res.success.yesterday;
        int TodayWeatherId = t.weatherId;

        sharedViewModel.setWeatherId(TodayWeatherId);

        Log.d("HomeWeatherFragment", "Weather ID (" + TodayWeatherId + ")를 ViewModel에 저장했습니다.");
        Log.d("CHECK",
                "today.min=" + t.temp.min +
                        " today.max=" + t.temp.max +
                        " round(min)=" + round(t.temp.min) +
                        " round(max)=" + round(t.temp.max));

        Log.d("CHECK",
                "yesterday.min=" + y.temp.min +
                        " yesterday.max=" + y.temp.max +
                        " round(min)=" + round(y.temp.min) +
                        " round(max)=" + round(y.temp.max));

        // 현재 온도
        tvTemp.setText(toTemp(c.temp));
        ivWeather.setImageResource(getWeatherIcon(c.icon));

        // 체감온도
        tvFeel.setText(toTemp(t.feels_like));

        // 강수확률
        int popPercent = round(t.pop * 100);
        tvPop.setText(popPercent + "%");

        // 미세먼지
        tvPm.setText(t.pm10text);

        // 오늘 최고/최저
        int todayHigh = round(t.temp.max);
        int todayLow = round(t.temp.min);

        int yHigh = round(y.temp.max);
        int yLow = round(y.temp.min);

        tvYesterdayHigh.setText(todayHigh + "°");
        tvYesterdayLow.setText(todayLow + "°");

        applyDiffIcon(ivYesterdayHigh, todayHigh - yHigh);
        applyDiffIcon(ivYesterdayLow, todayLow - yLow);

        applyBackground(c.icon);
    }

    private void applyBackground(String icon) {
        Fragment parent = getParentFragment();
        if (parent == null || parent.getView() == null) return;

        View root = parent.getView().findViewById(R.id.home_root);
        if(root == null) return;

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (icon.equals("09d") || icon.equals("09n")
                || icon.equals("10d") || icon.equals("10n")) {

            root.setBackgroundResource(R.drawable.bg_weather_rainy);
            return;
        }

        if (hour >= 18 || hour < 6) {
            root.setBackgroundResource(R.drawable.bg_weather_night);
            return;
        }

        switch (icon) {

            case "01d":
            case "01n":
                root.setBackgroundResource(R.drawable.bg_weather_sunny);
                break;

            case "02d": case "02n":
            case "03d": case "03n":
            case "04d": case "04n":
                root.setBackgroundResource(R.drawable.bg_weather_cloudy);
                break;

            default:
                root.setBackgroundResource(R.drawable.bg_weather_cloudy);
                break;
        }
    }

    private int round(double v) {
        return (int) Math.round(v);
    }

    private String toTemp(double v) {
        return round(v) + "°";
    }

    private int getWeatherIcon(String icon) {
        switch (icon) {

            // 맑음 (Sunny)
            case "01d":
            case "01n":
                return R.drawable.ic_weather_sunny;

            // 구름 조금 (Partly Cloudy)
            case "02d":
            case "02n":
                return R.drawable.ic_weather_cloudy_day;

            // 흐림 (Cloudy)
            case "03d": case "03n":
            case "04d": case "04n":
                return R.drawable.ic_weather_cloudy;

            // 비 (Rain)
            case "09d": case "09n":
            case "10d": case "10n":
                return R.drawable.ic_weather_rainy;

            // 눈, 안개, 천둥 등 → 아이콘 없으니 흐림으로 통일
            case "11d": case "11n":
            case "13d": case "13n":
            case "50d": case "50n":
            default:
                return R.drawable.ic_weather_cloudy;
        }
    }

    private void applyDiffIcon(ImageView iv, int diff) {
        if (diff > 0) iv.setImageResource(R.drawable.ic_arrow_up);
        else if (diff < 0) iv.setImageResource(R.drawable.ic_arrow_down);
        else iv.setImageResource(R.drawable.ic_same);
    }
}
