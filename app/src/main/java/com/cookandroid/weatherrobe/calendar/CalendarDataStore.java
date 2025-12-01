package com.cookandroid.weatherrobe.calendar;

import java.util.HashMap;
import java.util.Map;

public class CalendarDataStore {

    public static Map<String, String> weatherMap = new HashMap<>();
    public static Map<String, String> answerMap = new HashMap<>();

    static {
        weatherMap.put("2025-01-15", "최고 16° / 최저 1°");
        answerMap.put("2025-01-15", "😃적당");
    }

}
