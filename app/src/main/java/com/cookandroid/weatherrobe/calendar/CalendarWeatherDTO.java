package com.cookandroid.weatherrobe.calendar;

import java.util.List;

// 실제 weather 객체의 구조 그대로 DTO 작성
public class CalendarWeatherDTO {

    private String date;
    private String icon;
    private String weather;
    private double temp_max;
    private double temp_min;
    private String feeling_status;
    private String text;
    private List<String> keywords;

    public String getDate() { return date; }
    public String getIcon() { return icon; }
    public String getWeather() { return weather; }
    public double getTemp_max() { return temp_max; }
    public double getTemp_min() { return temp_min; }
    public String getFeeling_status() { return feeling_status; }
    public String getText() { return text; }
    public List<String> getKeywords() { return keywords; }
}
