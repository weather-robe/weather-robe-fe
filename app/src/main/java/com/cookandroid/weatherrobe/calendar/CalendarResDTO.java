package com.cookandroid.weatherrobe.calendar;

import java.util.List;

public class CalendarResDTO {

    // 캘린더 전체 조회용 DTO
    public static class CalendarItem {

        private String date;            // "2025-11-01"
        private String icon;            // "01n"
        private String weather;         // "Rain"
        private double temp_max;        // 최고기온
        private double temp_min;        // 최저기온
        private String feeling_status;  // "적당"
        private List<String> keywords;  // ["패딩","목도리","내복"]

        public String getDate() { return date; }
        public String getIcon() { return icon; }
        public String getWeather() { return weather; }
        public double getTemp_max() { return temp_max; }
        public double getTemp_min() { return temp_min; }
        public String getFeeling_status() { return feeling_status; }
        public List<String> getKeywords() { return keywords; }
    }

    // 캘린더 상세 조회용 DTO
    public static class CalendarDetailDTO {

        private String date;            // "2025-11-15"
        private String icon;            // "01n"
        private String weather;         // "Rain"
        private double temp_max;
        private double temp_min;
        private String feeling_status;
        private String text;            // 상세 설명
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
}
