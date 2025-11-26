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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HourlyFragment extends Fragment {

    private RecyclerView recyclerView;
    private HourlyAdapter adapter;
    private ArrayList<HourlyItem> hourlyList = new ArrayList<>();
    private HourlyApi api;
    private SharedPreferences prefs;

    public HourlyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hourly, container, false);

        // 테스트용 userId
        prefs = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        prefs.edit().putInt("userId", 1).apply();

        recyclerView = view.findViewById(R.id.recycler_weather);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        adapter = new HourlyAdapter(hourlyList);
        recyclerView.setAdapter(adapter);

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

                hourlyList.clear();
                hourlyList.addAll(data.hourly);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<HourlyResponse> call, Throwable t) {
                Log.e("Hourly", "연결 실패: " + t.getMessage());
            }
        });
    }
}
