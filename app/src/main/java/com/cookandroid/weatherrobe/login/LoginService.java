package com.cookandroid.weatherrobe.login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("/v1/api/auth/signin")
    Call<LoginResponse> login(@Body LoginRequest body);
}
