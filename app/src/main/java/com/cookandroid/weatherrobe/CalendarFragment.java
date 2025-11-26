package com.cookandroid.weatherrobe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {

    private GridView gridView;
    private CalendarAdapter calendarAdapter;

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

        TextView monthTitle = view.findViewById(R.id.header_title);
        gridView = view.findViewById(R.id.gridView); // ★ gridView 연결

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        // 헤더 텍스트 설정
        monthTitle.setText(year + "년 " + (month + 1) + "월");

        // 달력 데이터 구성
        List<DayItem> calendarDays = buildCalendarDays(year, month);

        // 어댑터 연결
        calendarAdapter = new CalendarAdapter(requireContext(), calendarDays);
        gridView.setAdapter(calendarAdapter);
    }


    // 달력 데이터 구성
    private List<DayItem> buildCalendarDays(int year, int month) {

        List<DayItem> list = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 앞 공백
        for (int i = 1; i < firstDayOfWeek; i++) {
            list.add(new DayItem("", false, false));
        }

        // 오늘 날짜 체크
        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayDay = today.get(Calendar.DAY_OF_MONTH);

        // 날짜 채우기
        for (int day = 1; day <= lastDay; day++) {

            boolean isToday = (year == todayYear &&
                    month == todayMonth &&
                    day == todayDay);

            list.add(new DayItem(String.valueOf(day), true, isToday));
        }

        return list;
    }
}
