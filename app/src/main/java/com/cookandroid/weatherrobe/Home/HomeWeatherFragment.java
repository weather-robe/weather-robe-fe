package com.cookandroid.weatherrobe.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cookandroid.weatherrobe.Home.model.Current;
import com.cookandroid.weatherrobe.Home.model.Today;
import com.cookandroid.weatherrobe.Home.model.Yesterday;
import com.cookandroid.weatherrobe.R;
import com.cookandroid.weatherrobe.RetrofitClient;
import com.cookandroid.weatherrobe.hourly.HourlyRequest;
import com.google.gson.Gson;
import com.cookandroid.weatherrobe.Home.model.SharedWeatherViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeWeatherFragment extends Fragment {
    private SharedWeatherViewModel sharedViewModel; // 전역 변수 용으로 필요 feat.수현
    private ImageView ivWeather, ivYesterdayHigh, ivYesterdayLow, ivEdit;
    private TextView tvTemp, tvYesterdayHigh, tvYesterdayLow;

    private Today today;
    private Current current;
    private Yesterday yesterday;

    private FrameLayout slot1, slot2, slot3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_weather, container, false);

        sharedViewModel = new ViewModelProvider(requireActivity()) // 전역 변수 용으로 필요 feat.수현
                .get(SharedWeatherViewModel.class);

        initViews(view);
        loadWeatherData();

        return view;
    }

    private void initViews(View v) {
        ivWeather = v.findViewById(R.id.iv_weather);

        tvTemp = v.findViewById(R.id.tv_temp);

        tvYesterdayHigh = v.findViewById(R.id.tv_yesterday_high);
        tvYesterdayLow = v.findViewById(R.id.tv_yesterday_low);

        ivYesterdayHigh = v.findViewById(R.id.iv_yesterday_up);
        ivYesterdayLow = v.findViewById(R.id.iv_yesterday_down);

        slot1 = v.findViewById(R.id.card_slot_1);
        slot2 = v.findViewById(R.id.card_slot_2);
        slot3 = v.findViewById(R.id.card_slot_3);

        ivEdit = v.findViewById(R.id.iv_edit);
        ivEdit.setOnClickListener(view -> {
            SelectOptionDialogFragment dialog =
                    new SelectOptionDialogFragment(selectedKeys -> applySelectedOptions(selectedKeys));

            dialog.show(getParentFragmentManager(), "select_options");
        });
    }

    /** 옵션 선택 후 홈 카드 반영 */
    private void applySelectedOptions(List<String> keys) {
        FrameLayout[] slots = { slot1, slot2, slot3 };

        for (int i = 0; i < keys.size(); i++) {
            FrameLayout container = slots[i];
            container.removeAllViews();

            int layoutRes = getCardLayout(keys.get(i));
            View card = LayoutInflater.from(container.getContext())
                    .inflate(layoutRes, container, false);


            View cardRoot = card.findViewById(R.id.card_root);
            cardRoot.setBackground(null);

            bindCardData(card, keys.get(i));
            applyWhiteText(card);    // 글씨 흰색으로 변경

            container.addView(card);
        }
    }

    /** 홈화면 날씨 텍스트 흰색으로 설정 */
    private void applyWhiteText(View card) {
        TextView title = card.findViewById(R.id.card_title);
        TextView value = card.findViewById(R.id.card_value);

        if (title != null) title.setTextColor(0xFFFFFFFF);
        if (value != null) value.setTextColor(0xFFFFFFFF);
    }

    /** 카드 타입 매핑 */
    private int getCardLayout(String key) {
        switch (key) {
            case "FEEL": return R.layout.home_feel_card_item;
            case "POP": return R.layout.home_pop_card_item;
            case "PM10": return R.layout.home_pm_card_item;
            case "PM25": return R.layout.home_pm25_card_item;
            case "HUMIDITY": return R.layout.home_humidity_card_item;
            case "WIND": return R.layout.home_wind_card_item;
            case "RAIN": return R.layout.home_rain_card_item;
        }
        return R.layout.home_feel_card_item;
    }

    /** 카드 데이터 바인딩 */
    private void bindCardData(View card, String key) {
        ImageView icon = card.findViewById(R.id.card_icon);
        TextView value = card.findViewById(R.id.card_value);
        TextView title = card.findViewById(R.id.card_title);

        switch (key) {
            case "FEEL":
                title.setText("체감온도");
                value.setText(toTemp(today.feels_like));
                break;

            case "POP":
                title.setText("강수확률");
                value.setText((int)(today.pop * 100) + "%");
                break;

            case "PM10":
                title.setText("미세먼지");
                value.setText(today.pm10text);
                icon.setImageResource(getPmIcon(today.pm10text));
                break;

            case "PM25":
                title.setText("초미세먼지");
                value.setText(today.pm25text);
                icon.setImageResource(getPmIcon(today.pm25text));
                break;

            case "HUMIDITY":
                title.setText("습도");
                value.setText(today.humidity + "%");
                break;

            case "WIND":
                title.setText("풍속");
                value.setText(today.wind_speed + "m/s");
                break;

            case "RAIN":
                title.setText("강수량");
                value.setText(today.rain + "mm");
                break;
        }
    }

    private int getPmIcon(String text) {
        if (text == null) return R.drawable.ic_pm_normal;

        switch (text) {
            case "좋음":
                return R.drawable.ic_pm_good;
            case "보통":
                return R.drawable.ic_pm_normal;
            case "나쁨":
                return R.drawable.ic_pm_bad;
            case "매우 나쁨":
                return R.drawable.ic_pm_very_bad;
            default:
                return R.drawable.ic_pm_normal;
        }
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
                if (!res.isSuccessful() || res.body() == null) return;

                current = res.body().success.current;
                today = res.body().success.today;
                yesterday = res.body().success.yesterday;

                updateWeatherUI();
                setSharedViewModelValue(today.weatherId, today.feedback);
                // 홈 화면 기본 3개 반영
                applySelectedOptions(Arrays.asList("FEEL", "POP", "PM10"));
            }

            @Override
            public void onFailure(Call<HomeWeatherResponse> call, Throwable t) {
                Log.e("WeatherAPI", t.getMessage());
            }
        });
    }
    private void updateWeatherUI() {
        tvTemp.setText(toTemp(current.temp));
        ivWeather.setImageResource(getWeatherIcon(current.icon));

        int todayHigh = round(today.temp.max);
        int todayLow = round(today.temp.min);

        int yHigh = round(yesterday.temp.max);
        int yLow = round(yesterday.temp.min);

        tvYesterdayHigh.setText(todayHigh + "°");
        tvYesterdayLow.setText(todayLow + "°");

        applyDiffIcon(ivYesterdayHigh, todayHigh - yHigh);
        applyDiffIcon(ivYesterdayLow, todayLow - yLow);

        applyBackground(current.icon);
    }

    private void applyBackground(String icon) {
        Fragment parent = getParentFragment();
        if (parent == null || parent.getView() == null) return;

        View root = parent.getView().findViewById(R.id.home_root);
        if(root == null) return;

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (icon.startsWith("09") || icon.startsWith("10")) {

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
        }
    }

    private int round(double v) { return (int) Math.round(v); }
    private String toTemp(double v) { return round(v) + "°"; }

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

    private void setSharedViewModelValue(int weatherId, String feedback) {
        sharedViewModel.setWeatherId(weatherId);
        sharedViewModel.setWeatherFeedback(feedback);
    }
}
