package com.cookandroid.weatherrobe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        // 서버 날짜 불러오기
        loadMonthFromServer(view);
    }

    private void loadMonthFromServer(View view) {

        TextView monthTitle = view.findViewById(R.id.header_title);

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
                // 서버 요청 실패 처리: 현재 연결 실패 시 아무 변화 없음
            }
        });
    }
}
