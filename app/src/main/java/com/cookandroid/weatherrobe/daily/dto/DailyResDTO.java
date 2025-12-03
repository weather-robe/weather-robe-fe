package com.cookandroid.weatherrobe.daily.dto;

import java.util.Date;
import java.util.List;

public class DailyResDTO {

    public static class User {
        private int userId;

        public User(int userId) {
            this.userId = userId;
        }

        public int getUserId() { return userId; }
    }

    public static class Daily {
        private int weatherId;
        private Date date;
        private String icon;
        private String weather;
        private Temp temp;
        private double pop;

        public Daily(int weatherId, Date date, String weather, String icon, Temp temp, double pop) {
            this.weatherId = weatherId;
            this.date = date;
            this.weather = weather;
            this.icon = icon;
            this.temp = temp;
            this.pop = pop;
        }

        public int getWeatherId() { return weatherId; }
        public Date getDate() { return date; }
        public String getIcon() { return icon; }
        public String getWeather() { return weather; }
        public Temp getTemp() { return temp; }
        public double getPop() { return pop; }
    }

    public static class TempDefault {
        private double max;
        private double min;

        public TempDefault(double max, double min) {
            this.max = max;
            this.min = min;
        }

        public double getMax() { return max; }
        public double getMin() { return min; }
    }

    public static class Temp {
        private double max;
        private double min;
        private double morning;
        private double afternoon;

        public Temp(double max, double min, double morning, double afternoon) {
            this.max = max;
            this.min = min;
            this.morning = morning;
            this.afternoon = afternoon;
        }

        public double getMax() {
            return max;
        }

        public double getMin() {
            return min;
        }

        public double getMorning() {
            return morning;
        }

        public double getAfternoon() {
            return afternoon;
        }
    }
    
    public static class Yesterday {
        private int weatherId;
        private Date date;
        private TempDefault temp;

        public Yesterday(int weatherId, Date date, TempDefault temp) {
            this.weatherId = weatherId;
            this.date = date;
            this.temp = temp;
        }

        public int getWeatherId() { return weatherId; }
        public Date getDate() { return date; }
        public TempDefault getTemp() { return temp; }
    }

    public static class PostDailyDTO {
        private User user;
        private List<Daily> daily;
        private Yesterday yesterday;

        public PostDailyDTO(User user, List<Daily> daily, Yesterday yesterday) {
            this.user = user;
            this.daily = daily;
            this.yesterday = yesterday;
        }

        public User getUser() { return user; }
        public List<Daily> getDaily() { return daily; }
        public Yesterday getYesterday() { return yesterday; }
    }
}

