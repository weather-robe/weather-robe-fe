package com.cookandroid.weatherrobe.signup;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface signupService {

    @POST("/v1/api/auth/signup")
    Call<Map<String, Object>> signup(@Body Map<String, String> body);
}
