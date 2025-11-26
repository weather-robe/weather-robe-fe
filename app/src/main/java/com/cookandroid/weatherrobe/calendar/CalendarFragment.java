package com.cookandroid.weatherrobe.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cookandroid.weatherrobe.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {

    private GridView gridView;
    private CalendarAdapter calendarAdapter;

    private int currentYear;
    private int currentMonth;

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
        gridView = view.findViewById(R.id.gridView);

        // 현재 날짜 기준으로 시작
        Calendar cal = Calendar.getInstance();
        currentYear = cal.get(Calendar.YEAR);
        currentMonth = cal.get(Calendar.MONTH);

        updateCalendar(view);

        // 스와이프 이벤트
        gridView.setOnTouchListener(new View.OnTouchListener() {
            float startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        return true;

                    case MotionEvent.ACTION_UP:
                        float endX = event.getX();

                        // 오른쪽 -> 왼쪽 : 다음 달
                        if (startX - endX > 150) {
                            moveToNextMonth(view);
                        }

                        // 왼쪽 -> 오른쪽 : 이전 달
                        else if (endX - startX > 150) {
                            moveToPreviousMonth(view);
                        }

                        return true;
                }
                return false;
            }
        });
    }


    // 달력 갱신 함수
    private void updateCalendar(View view) {

        TextView monthTitle = view.findViewById(R.id.header_title);

        // 헤더 텍스트 변경
        monthTitle.setText(currentYear + "년 " + (currentMonth + 1) + "월");

        // 날짜 리스트 생성
        List<DayItem> calendarDays = buildCalendarDays(currentYear, currentMonth);

        // 어댑터 적용
        calendarAdapter = new CalendarAdapter(requireContext(), calendarDays);
        gridView.setAdapter(calendarAdapter);
    }


    // 이전/다음달 이동
    private void moveToNextMonth(View view) {
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        updateCalendar(view);
    }

    private void moveToPreviousMonth(View view) {
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }
        updateCalendar(view);
    }

    // 날짜 리스트 생성
    private List<DayItem> buildCalendarDays(int year, int month) {

        List<DayItem> list = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 1일 시작 요일까지 앞에 빈칸 추가
        for (int i = 1; i < firstDayOfWeek; i++) {
            list.add(new DayItem("", false, false));
        }

        // 오늘 날짜 비교용
        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayDay = today.get(Calendar.DAY_OF_MONTH);

        // 1~마지막 날짜 생성
        for (int day = 1; day <= lastDay; day++) {

            boolean isToday = (year == todayYear &&
                    month == todayMonth &&
                    day == todayDay);

            list.add(new DayItem(String.valueOf(day), true, isToday));
        }

        return list;
    }
}
