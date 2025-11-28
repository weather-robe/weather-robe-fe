package com.cookandroid.weatherrobe.daily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.weatherrobe.R;
import java.util.ArrayList;
import java.util.List;

public class DailyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_weather);

        List<DailyWeatherData> dataList = createDummyWeatherData();

        boolean hasYesterdayData = true;

        DailyWeatherAdapter weatherAdapter = new DailyWeatherAdapter(dataList, hasYesterdayData);

        recyclerView.setAdapter(weatherAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private List<DailyWeatherData> createDummyWeatherData() {
        List<DailyWeatherData> dataList = new ArrayList<>();
        int sunny = R.drawable.ic_weather_sunny;
        int cloudy = R.drawable.ic_weather_cloudy;

        dataList.add(new DailyWeatherData("오늘", "11월 4일", sunny, "1°", "12°"));
        dataList.add(new DailyWeatherData("토", "11월 5일", cloudy, "2°", "10°"));
        dataList.add(new DailyWeatherData("일", "11월 6일", sunny, "3°", "11°"));
        dataList.add(new DailyWeatherData("월", "11월 7일", cloudy, "4°", "9°"));
        dataList.add(new DailyWeatherData("화", "11월 8일", sunny, "5°", "12°"));
        dataList.add(new DailyWeatherData("수", "11월 9일", cloudy, "6°", "13°"));
        dataList.add(new DailyWeatherData("목", "11월 10일", sunny, "7°", "14°"));
        dataList.add(new DailyWeatherData("금", "11월 11일", sunny, "7°", "14°"));
        return dataList;
    }
}