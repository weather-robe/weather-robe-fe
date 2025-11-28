package com.cookandroid.weatherrobe.hourly;

import java.util.List;

public class HourlyResponse {
    public String resultType;
    public Object error;
    public SuccessData success;

    public static class SuccessData {
        public User user;
        public List<HourlyItem> hourly;
        public String pm10text;
        public String pm25text;
        public int pm10;
        public int pm25;

        public static class User {
            public int userId;
        }
    }
}
