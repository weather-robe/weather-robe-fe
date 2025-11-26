package com.cookandroid.weatherrobe;

public class HourlyItem {
    public String time;
    public int iconRes;
    public String temp;
    public String feel;
    public String rainProb;
    public String rainAmount;
    public String humidity;
    public String wind;

    public HourlyItem(String time, int iconRes, String temp, String feel, String rainProb, String rainAmount, String humidity, String wind) {
        this.time = time;
        this.iconRes = iconRes;
        this.temp = temp;
        this.feel = feel;
        this.rainProb = rainProb;
        this.rainAmount = rainAmount;
        this.humidity = humidity;
        this.wind = wind;
    }

}
