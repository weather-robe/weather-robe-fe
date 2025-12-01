package com.cookandroid.weatherrobe.calendar;

import android.os.Bundle;
import android.util.Log;
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
import com.cookandroid.weatherrobe.RetrofitClient;
import com.cookandroid.weatherrobe.common.CommonApiResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarFragment extends Fragment {

    private GridView gridView;
    private CalendarAdapter calendarAdapter;

    private CalendarService calendarService;
    private List<CalendarResDTO.CalendarItem> calendarData = new ArrayList<>();

    private int currentYear;
    private int currentMonth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrofit Calendar API 초기화 (기존 주석 유지)
        calendarService = RetrofitClient
                .getClient("https://weather-robe.site")
                .create(CalendarService.class);
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

        gridView = view.findViewById(R.id.gridView);

        // 현재 날짜 정보 불러오기 (기존 주석 유지)
        Calendar cal = Calendar.getInstance();
        currentYear = cal.get(Calendar.YEAR);
        currentMonth = cal.get(Calendar.MONTH);

        // 달력 화면 초기 구성 (기존 주석 유지)
        updateCalendar(view);

        // 전체 조회 API 호출 (기존 주석 유지)
        fetchCalendarList(1);

        // 스와이프 이벤트 처리 (월 이동) (기존 주석 유지)
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

                        if (startX - endX > 150) moveToNextMonth(view);
                        else if (endX - startX > 150) moveToPreviousMonth(view);

                        return true;
                }
                return false;
            }
        });
    }

    // 달력 UI 갱신 및 날짜 클릭 이벤트 설정 (기존 주석 유지)
    private void updateCalendar(View view) {

        TextView monthTitle = view.findViewById(R.id.header_title);
        monthTitle.setText(currentYear + "년 " + (currentMonth + 1) + "월");

        List<DayItem> days = buildCalendarDays(currentYear, currentMonth);

        calendarAdapter = new CalendarAdapter(requireContext(), days);
        gridView.setAdapter(calendarAdapter);

        // 날짜 클릭 → 상세조회 API 호출 후 모달 띄우기 (기존 주석 유지)
        calendarAdapter.setOnDayClickListener((year, month, day) -> {

            String dateStr = String.format("%04d-%02d-%02d", year, month + 1, day);
            Log.d("CAL_CLICK", "날짜 클릭됨: " + dateStr);   // 로그

            // 상세 조회 API 실행 (기존 주석 유지)
            fetchCalendarDetail(1, dateStr);
        });
    }

    // 다음 달로 이동 (기존 주석 유지)
    private void moveToNextMonth(View view) {
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        updateCalendar(view);
        fetchCalendarList(1);
    }

    // 이전 달로 이동 (기존 주석 유지)
    private void moveToPreviousMonth(View view) {
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }
        updateCalendar(view);
        fetchCalendarList(1);
    }

    // 현재 월의 날짜 리스트 생성 (기존 주석 유지)
    private List<DayItem> buildCalendarDays(int year, int month) {
        List<DayItem> list = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);

        int firstDay = c.get(Calendar.DAY_OF_WEEK);
        int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 빈 칸 채우기 (기존 주석 유지)
        for (int i = 1; i < firstDay; i++) {
            list.add(new DayItem("", false, false));
        }

        // 오늘 날짜 판단 (기존 주석 유지)
        Calendar today = Calendar.getInstance();
        int tYear = today.get(Calendar.YEAR);
        int tMonth = today.get(Calendar.MONTH);
        int tDay = today.get(Calendar.DAY_OF_MONTH);

        // 날짜 생성 (기존 주석 유지)
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

    // 전체 조회 API (기존 주석 유지)
    private void fetchCalendarList(int userId) {

        calendarService.getCalendarList(userId)
                .enqueue(new Callback<CommonApiResponse<List<CalendarResDTO.CalendarItem>>>() {
                    @Override
                    public void onResponse(
                            Call<CommonApiResponse<List<CalendarResDTO.CalendarItem>>> call,
                            Response<CommonApiResponse<List<CalendarResDTO.CalendarItem>>> response) {

                        if (!isAdded() || response.body() == null) return;

                        if (response.isSuccessful() && response.body().isSuccessful()) {

                            Log.d("CAL_LIST", "캘린더 전체 조회 성공");  // 로그

                            calendarData = response.body().getSuccess();
                        }
                    }

                    @Override
                    public void onFailure(Call<CommonApiResponse<List<CalendarResDTO.CalendarItem>>> call,
                                          Throwable t) {
                        Log.e("CAL_LIST", "캘린더 전체 조회 실패: " + t.getMessage());   // 로그
                    }
                });
    }

    // 상세 조회 API (기존 주석 유지)
    private void fetchCalendarDetail(int userId, String dateStr) {

        Log.d("CAL_DETAIL", "상세조회 API 실행됨: " + dateStr);   // 로그

        calendarService.getCalendarDetail(userId, dateStr)
                .enqueue(new Callback<CommonApiResponse<CalendarResDTO.CalendarDetailDTO>>() {

                    @Override
                    public void onResponse(
                            Call<CommonApiResponse<CalendarResDTO.CalendarDetailDTO>> call,
                            Response<CommonApiResponse<CalendarResDTO.CalendarDetailDTO>> response) {

                        if (!isAdded() || response.body() == null) {
                            Log.e("CAL_DETAIL", "응답 body null");   // 로그
                            return;
                        }

                        Log.d("CAL_DETAIL", "상세조회 응답 코드: " + response.code());   // 로그

                        if (response.isSuccessful() && response.body().isSuccessful()) {

                            CalendarResDTO.CalendarDetailDTO detail =
                                    response.body().getSuccess();

                            // 모달 띄우기 (기존 주석 유지)
                            ModalCalendarDialog dialog =
                                    ModalCalendarDialog.newInstance(dateStr);

                            dialog.setDetailData(detail);
                            dialog.show(getParentFragmentManager(), "cal_detail");
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<CommonApiResponse<CalendarResDTO.CalendarDetailDTO>> call,
                            Throwable t) {
                        Log.e("CAL_DETAIL", "상세 조회 실패: " + t.getMessage());   // 로그
                    }
                });
    }
}
