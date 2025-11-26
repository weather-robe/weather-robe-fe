package com.cookandroid.weatherrobe.calendar;
// 날짜 데이터 객체

public class DayItem {
    public String dayText;
    public boolean isDay;   // 빈칸인지 실제 날짜인지
    public boolean isToday; // 오늘 날짜인지

    public DayItem(String dayText, boolean isDay, boolean isToday) {
        this.dayText = dayText;
        this.isDay = isDay;
        this.isToday = isToday;
    }
}
