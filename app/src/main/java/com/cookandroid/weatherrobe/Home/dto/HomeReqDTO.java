package com.cookandroid.weatherrobe.Home.dto;

public class HomeReqDTO {
    public class PostHomeDTO {
        private String feedback;

        public PostHomeDTO(String feedback) {
            this.feedback = feedback;
        }

        public String getFeedback() { return feedback; }
    }
}
