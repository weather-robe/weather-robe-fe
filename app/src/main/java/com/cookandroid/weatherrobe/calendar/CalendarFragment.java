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

        gridView = view.findViewById(R.id.gridView);

        Calendar cal = Calendar.getInstance();
        currentYear = cal.get(Calendar.YEAR);
        currentMonth = cal.get(Calendar.MONTH);

        updateCalendar(view);

        // 🟦 스와이프 이벤트
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

                        // 오른쪽 → 왼쪽 (다음 달)
                        if (startX - endX > 150) {
                            moveToNextMonth(view);
                        }
                        // 왼쪽 → 오른쪽 (이전 달)
                        else if (endX - startX > 150) {
                            moveToPreviousMonth(view);
                        }

                        return true;
                }
                return false;
            }
        });
    }

    // 🟦 달력 갱신
    private void updateCalendar(View view) {
        TextView monthTitle = view.findViewById(R.id.header_title);
        monthTitle.setText(currentYear + "년 " + (currentMonth + 1) + "월");

        List<DayItem> days = buildCalendarDays(currentYear, currentMonth);

        calendarAdapter = new CalendarAdapter(requireContext(), days);
        gridView.setAdapter(calendarAdapter);

        // 날짜 클릭 → 모달 띄우기
        calendarAdapter.setOnDayClickListener((year, month, day) -> {
            ModalCalendarDialog dialog =
                    ModalCalendarDialog.newInstance(year, month, day);
            dialog.show(getParentFragmentManager(), "calendar_modal");
        });
    }

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

        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);

        int firstDay = c.get(Calendar.DAY_OF_WEEK);
        int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 앞 빈칸
        for (int i = 1; i < firstDay; i++) {
            list.add(new DayItem("", false, false));
        }

        // 오늘
        Calendar today = Calendar.getInstance();
        int tYear = today.get(Calendar.YEAR);
        int tMonth = today.get(Calendar.MONTH);
        int tDay = today.get(Calendar.DAY_OF_MONTH);

        // 실제 날짜 생성
        for (int d = 1; d <= lastDay; d++) {
            boolean isToday = (year == tYear && month == tMonth && d == tDay);

            DayItem item = new DayItem(String.valueOf(d), true, isToday);

            item.year = year;
            item.month = month;
            item.day = d;

            list.add(item);
        }

        return list;
    }
}
