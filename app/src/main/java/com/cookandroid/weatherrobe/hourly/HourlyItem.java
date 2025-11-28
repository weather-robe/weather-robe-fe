package com.cookandroid.weatherrobe.hourly;

public class HourlyItem {
    public int weatherId;
    public String date;
    public String time;
    public double temp;
    public double feels_like;
    public String weather;
    public double pop;
    public Double rain;
    public int humidity;
    public String wind_text;
    public double wind_speed;
    public int wind_deg;
    public String icon;

    public HourlyItem(int weatherId, String date, String time, double temp, double feels_like, String weather, double pop, Double rain, int humidity, String wind_text, double wind_speed, int wind_deg, String icon) {
        this.weatherId = weatherId;
        this.date = date;
        this.time = time;
        this.temp = temp;
        this.feels_like = feels_like;
        this.weather = weather;
        this.pop = pop;
        this.rain = rain;
        this.humidity = humidity;
        this.wind_text = wind_text;
        this.wind_speed = wind_speed;
        this.wind_deg = wind_deg;
        this.icon = icon;
    }

}
