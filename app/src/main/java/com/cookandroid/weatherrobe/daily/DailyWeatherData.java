package com.cookandroid.weatherrobe.daily;

public class DailyWeatherData {
    private String dayLabel;
    private String dateValue;
    private int weatherIconRes;
    private String tempMin;
    private String tempMax;

    public DailyWeatherData(String dayLabel, String dateValue, int weatherIconRes, String tempMin, String tempMax) {
        this.dayLabel = dayLabel;
        this.dateValue = dateValue;
        this.weatherIconRes = weatherIconRes;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    public String getDayLabel() { return dayLabel; }
    public String getDateValue() { return dateValue; }
    public int getWeatherIconRes() { return weatherIconRes; }
    public String getTempMin() { return tempMin; }
    public String getTempMax() { return tempMax; }
}
