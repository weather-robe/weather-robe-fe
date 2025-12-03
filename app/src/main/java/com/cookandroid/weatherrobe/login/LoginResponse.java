package com.cookandroid.weatherrobe.login;

public class LoginResponse {
    public String resultType;
    public ErrorData error;
    public SuccessData success;

    public static class ErrorData {
        public String errorCode;
        public String reason;
        public Object data;
    }

    public static class SuccessData {
        public int userId;
        public String createdAt;
        public String updatedAt;
    }
}
