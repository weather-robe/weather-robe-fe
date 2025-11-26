package com.cookandroid.weatherrobe.hourly;

import java.util.List;

public class HourlyResponse {
    public String resultType;
    public Object error;
    public SuccessData success;
    public List<HourlyItem> hourly;
    public int pm10;
    public int pm25;

    public static class SuccessData {
        public User user;

        public static class User {
            public int userId;
        }
    }
}
