package com.cookandroid.weatherrobe.calendar;

public class DayItem {
    public String dayText;
    public boolean isValid;
    public boolean isToday;

    public int year;
    public int month;
    public int day;

    public DayItem(String dayText, boolean isValid, boolean isToday) {
        this.dayText = dayText;
        this.isValid = isValid;
        this.isToday = isToday;
    }
}
