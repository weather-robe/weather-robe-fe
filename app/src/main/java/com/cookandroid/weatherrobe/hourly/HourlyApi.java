package com.cookandroid.weatherrobe.hourly;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface HourlyApi {

    @GET("/v1/api/user/{userId}/weather/hourly")
    Call<HourlyResponse> getHourlyWeather(
            @Path("userId") int userId,
            @Query("lat") double lat,
            @Query("lon") double lon
    );
}
