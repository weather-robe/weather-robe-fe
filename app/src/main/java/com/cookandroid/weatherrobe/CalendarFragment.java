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

        // 캘린더 전용 헤더 설정
        setupHeaderForCalendar(view);

        // 서버 날짜 불러오기
        loadMonthFromServer(view);
    }

    private void loadMonthFromServer(View view) {

        TextView monthTitle = view.findViewById(R.id.tvMonth);

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getMonthInfo().enqueue(new Callback<MonthResponse>() {
            @Override
            public void onResponse(Call<MonthResponse> call, Response<MonthResponse> response) {
                if (response.isSuccessful()) {
                    MonthResponse data = response.body();
                    String text = data.year + "년 " + data.month + "월";
                    monthTitle.setText(text);
                }
            }

            @Override
            public void onFailure(Call<MonthResponse> call, Throwable t) {
                // 실패 처리
            }
        });
    }

    private void setupHeaderForCalendar(View view) {
        ImageView leftIcon = view.findViewById(R.id.ivLeft);     // 왼쪽 햄버거 btn
        TextView monthTitle = view.findViewById(R.id.tvMonth);   // 현재 현재 날짜 영역
        ImageView rightIcon = view.findViewById(R.id.ivInfo);     // 오른쪽 더 보기 btn

        monthTitle.setText("2025년 10월");  // 현재 날짜 영역: 추후 API 연동 예정임. 임시데이터
    }
}
// 하단 네비바 추가 필요함!