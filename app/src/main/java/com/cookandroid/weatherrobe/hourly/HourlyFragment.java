package com.cookandroid.weatherrobe.hourly;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.cookandroid.weatherrobe.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HourlyFragment extends Fragment {

    private RecyclerView recyclerMain;
    private HourlyMainAdapter mainAdapter;
    private HourlyApi api;
    private SharedPreferences prefs;

    public HourlyFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hourly, container, false);

        prefs = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        prefs.edit().putInt("userId", 1).apply();

        recyclerMain = view.findViewById(R.id.recycler_main);
        recyclerMain.setLayoutManager(new LinearLayoutManager(getContext()));

        mainAdapter = new HourlyMainAdapter();
        recyclerMain.setAdapter(mainAdapter);

        api = RetrofitClient.getClient("https://api.weather-robe.kro.kr/")
                .create(HourlyApi.class);

        loadWeather();

        return view;
    }

    private void loadWeather() {
        SharedPreferences prefs =
                requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        int userId = prefs.getInt("userId", -1);
        HourlyRequest req = new HourlyRequest(37.5, 127.0);

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

                // 날짜 정보 설정
                String rawDate = data.hourly.get(0).date;
                try {
                    SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                    SimpleDateFormat displayDate = new SimpleDateFormat("M월 d일", Locale.KOREA);
                    SimpleDateFormat displayDay = new SimpleDateFormat("EEEE", Locale.KOREA);

                    Date parsedDate = serverFormat.parse(rawDate);
                    mainAdapter.setDateInfo(
                            displayDate.format(parsedDate),
                            displayDay.format(parsedDate)
                    );
                } catch (Exception e) {
                    Log.e("Hourly", "날짜 파싱 오류: " + e.getMessage());
                }

                // 시간별 날씨 데이터 설정
                mainAdapter.setHourlyData(new ArrayList<>(data.hourly));

                // 미세먼지 데이터 설정
                mainAdapter.setPmData(data.pm10text, data.pm10, data.pm25text, data.pm25);

                // UI 갱신
                mainAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<HourlyResponse> call, Throwable t) {
                Log.e("Hourly", "연결 실패: " + t.getMessage());
            }
        });
    }
}
