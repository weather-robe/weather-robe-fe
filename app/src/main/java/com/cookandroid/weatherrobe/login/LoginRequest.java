package com.cookandroid.weatherrobe.login;

public class LoginRequest {
    public String loginId;
    public String password;

    public LoginRequest(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
