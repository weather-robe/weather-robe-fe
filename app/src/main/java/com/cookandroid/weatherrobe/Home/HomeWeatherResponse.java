package com.cookandroid.weatherrobe.Home;

import com.cookandroid.weatherrobe.Home.model.Current;
import com.cookandroid.weatherrobe.Home.model.Today;
import com.cookandroid.weatherrobe.Home.model.Yesterday;

public class HomeWeatherResponse {

    public String resultType;
    public Object error;
    public SuccessData success;

    public static class SuccessData {
        public User user;
        public Current current;
        public Today today;
        public Yesterday yesterday;

        public static class User {
            public int id;
        }
    }

}
