package com.cookandroid.weatherrobe.daily.dto;

public class DailyReqDTO {

    public class PostDailyDTO {
        private double latitude;
        private double longitude;

        public PostDailyDTO(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
    }
}