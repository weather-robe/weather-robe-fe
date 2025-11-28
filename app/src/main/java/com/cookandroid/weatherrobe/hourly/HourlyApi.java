package com.cookandroid.weatherrobe.hourly;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.POST;


public interface HourlyApi {

    @POST("/v1/api/user/{userId}/weather/hourly")
    Call<HourlyResponse> getHourlyWeather(
            @Path("userId") int userId,
            @Body HourlyRequest request
    );
}
