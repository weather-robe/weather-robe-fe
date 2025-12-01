package com.cookandroid.weatherrobe.Home.dto;

import com.cookandroid.weatherrobe.daily.dto.DailyResDTO;

import java.util.List;

public class HomeResDTO {
    public static class User {
        private int userId;

        public User(int userId) {
            this.userId = userId;
        }

        public int getUserId() { return userId; }
    }
    public static class Daily {
        private int id;
        private String feedback;

        public Daily(int id, String feedback) {
            this.id = id;
            this.feedback = feedback;
        }

        public int getId() {
            return id;
        }

        public String getFeedback() {
            return feedback;
        }
    }
    public static class Weather {
        private String text;
        private List<String> keywords;

        public Weather(String text, List<String> keywords) {
            this.text = text;
            this.keywords = keywords;
        }

        public String getText() { return text; }
        public List<String> getKeywords() { return keywords; }
    }
    public static class PostKeywordDTO {
        private HomeResDTO.User user;
        private HomeResDTO.Weather weather;

        public PostKeywordDTO(HomeResDTO.User user, HomeResDTO.Weather weather){
            this.user = user;
            this.weather = weather;
        }

        public HomeResDTO.User getUser() { return user; }
        public HomeResDTO.Weather getWeather() { return weather; }
    }
    public static class PostImageDTO {
        private HomeResDTO.User user;
        private List<String> images;

        public PostImageDTO(HomeResDTO.User user, List<String> images) {
            this.user = user;
            this.images = images;
        }

        public HomeResDTO.User getUser() { return user; }
        public List<String> getImages() { return images; }
    }
    public static class PostFeedbackDTO {
        private HomeResDTO.User user;
        private HomeResDTO.Daily daily;

        public PostFeedbackDTO(HomeResDTO.User user, HomeResDTO.Daily daily) {
            this.user = user;
            this.daily = daily;
        }

        public HomeResDTO.User getUser() { return user; }
        public HomeResDTO.Daily getDaily() { return daily; }
    }
}
