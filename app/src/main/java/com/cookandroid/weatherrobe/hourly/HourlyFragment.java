package com.cookandroid.weatherrobe.hourly;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.weatherrobe.R;
import com.cookandroid.weatherrobe.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HourlyFragment extends Fragment {

    private RecyclerView recyclerView;
    private HourlyAdapter adapter;
    private ArrayList<HourlyItem> hourlyList = new ArrayList<>();
    private HourlyApi api;
    private SharedPreferences prefs;
    private TextView dateText, dayText;
    private ImageView pm10Icon, pm25Icon;
    private TextView pm10Value, pm25Value;
    private TextView pm10Comment2, pm25Comment2;
    private TextView pm10Comment3, pm25Comment3;
    private View pm10BarFill, pm25BarFill;

    public HourlyFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hourly, container, false);

        prefs = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        prefs.edit().putInt("userId", 1).apply();

        dateText = view.findViewById(R.id.txt_date);
        dayText = view.findViewById(R.id.txt_day);

        recyclerView = view.findViewById(R.id.recycler_weather);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        adapter = new HourlyAdapter(hourlyList);
        recyclerView.setAdapter(adapter);

        api = RetrofitClient.getClient("https://api.weather-robe.kro.kr/")
                .create(HourlyApi.class);

        View pm10Card = view.findViewById(R.id.pm10_card);
        View pm25Card = view.findViewById(R.id.pm25_card);

        pm10Icon = pm10Card.findViewById(R.id.pm_icon);
        pm25Icon = pm25Card.findViewById(R.id.pm_icon);

        pm10Value = pm10Card.findViewById(R.id.pm_status);
        pm25Value = pm25Card.findViewById(R.id.pm_status);

        pm10BarFill = pm10Card.findViewById(R.id.pm_bar_fill);
        pm25BarFill = pm25Card.findViewById(R.id.pm_bar_fill);

        pm10Comment2 = pm10Card.findViewById(R.id.pm_comment2);
        pm25Comment2 = pm25Card.findViewById(R.id.pm_comment2);

        pm10Comment3 = pm10Card.findViewById(R.id.pm_comment3);
        pm25Comment3 = pm25Card.findViewById(R.id.pm_comment3);

        TextView pm10Title = pm10Card.findViewById(R.id.pm_title);
        TextView pm25Title = pm25Card.findViewById(R.id.pm_title);

        pm10Title.setText("미세먼지");
        pm25Title.setText("초미세먼지");

        loadWeather();

        RecyclerView recycler = view.findViewById(R.id.recycler_weather);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);

        return view;

    }

    private void loadWeather() {
        SharedPreferences prefs =
                requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        int userId = prefs.getInt("userId", -1);
        HourlyRequest req = new HourlyRequest(37.5665, 126.9780);

        api.getHourlyWeather(userId, req).enqueue(new Callback<HourlyResponse>() {
            @Override
            public void onResponse(Call<HourlyResponse> call, Response<HourlyResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("Hourly", "응답 실패: " + response.code());
                    return;
                }

                HourlyResponse.SuccessData data = response.body().success;
                if (data == null || data.hourly == null) {
                    Log.e("Hourly", "success 또는 hourly 데이터 없음");
                    return;
                }

                hourlyList.clear();
                hourlyList.addAll(data.hourly);
                adapter.notifyDataSetChanged();

                String rawDate = data.hourly.get(0).date;

                try {
                    SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                    SimpleDateFormat displayDate = new SimpleDateFormat("M월 d일", Locale.KOREA);
                    SimpleDateFormat displayDay = new SimpleDateFormat("EEEE", Locale.KOREA);

                    Date parsedDate = serverFormat.parse(rawDate);

                    dateText.setText(displayDate.format(parsedDate));
                    dayText.setText(displayDay.format(parsedDate));

                } catch (Exception e) {
                    Log.e("Hourly", "날짜 파싱 오류: " + e.getMessage());
                }

                updatePmCard(pm10Icon, pm10Value, data.pm10text, data.pm10,
                        pm10BarFill, pm10Comment2, pm10Comment3, true);

                updatePmCard(pm25Icon, pm25Value, data.pm25text, data.pm25,
                        pm25BarFill, pm25Comment2, pm25Comment3, false);
            }

            @Override
            public void onFailure(Call<HourlyResponse> call, Throwable t) {
                Log.e("Hourly", "연결 실패: " + t.getMessage());
            }
        });
    }

    private void updatePmCard(ImageView icon,
                              TextView valueText,
                              String gradeText,
                              int value,
                              View barFill,
                              TextView comment2,
                              TextView comment3,
                              boolean isPm10) {

        valueText.setText(gradeText + " " + value + "㎍/㎥");
        icon.setImageResource(getPmIcon(gradeText));

        int goodLimit = isPm10 ? 30 : 15;
        comment2.setText(value <= goodLimit ? "이하" : "이상");

        comment3.setText(getActivityMessage(gradeText));

        barFill.setBackgroundColor(getBarColor(gradeText));
        updateBarWidth(barFill, value);
    }

    private String getActivityMessage(String grade) {
        switch (grade) {
            case "좋음": return "야외 활동에 적합";
            case "보통": return "몸상태에 따라 활동 유의";
            case "나쁨": return "가급적 실내 활동 권장";
            case "매우 나쁨": return "실외 활동 제한 및 마스크 착용 권장";
            default: return "활동 유의";
        }
    }

    private int getBarColor(String grade) {
        switch (grade) {
            case "좋음": return 0xFF8FDA92;
            case "보통": return 0xFFFFED65;
            case "나쁨": return 0xFFFF9800;
            case "매우 나쁨": return 0xFFFD675C;
            default: return 0xFF8FDA92;
        }
    }

    private void updateBarWidth(View fill, int value) {
        fill.post(() -> {
            int parentWidth = ((View) fill.getParent()).getWidth();
            float ratio = Math.min(value, 100) / 100f;
            ViewGroup.LayoutParams params = fill.getLayoutParams();
            params.width = (int) (parentWidth * ratio);
            fill.setLayoutParams(params);
        });
    }

    private int getPmIcon(String grade) {
        switch (grade) {
            case "좋음": return R.drawable.ic_pm_good;
            case "보통": return R.drawable.ic_pm_normal;
            case "나쁨": return R.drawable.ic_pm_bad;
            case "매우 나쁨": return R.drawable.ic_pm_very_bad;
            default: return R.drawable.ic_pm_normal;
        }
    }
}
