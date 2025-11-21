package com.cookandroid.weatherrobe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CalendarFragment extends Fragment {

    public CalendarFragment() {
        // 기본 생성자
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 캘린더 헤더 설정
        setupHeaderForCalendar(view);
    }

    private void setupHeaderForCalendar(View view) {
        ImageView mapIcon = view.findViewById(R.id.header_map_icon);
        TextView title = view.findViewById(R.id.header_title);

        mapIcon.setVisibility(View.GONE);
        title.setText("2025년 10월");
    }

// 하단 네비바 추가 필요함!