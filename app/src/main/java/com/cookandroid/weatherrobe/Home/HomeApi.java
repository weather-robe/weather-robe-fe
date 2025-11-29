package com.cookandroid.weatherrobe.Home;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HomeApi {
    @POST("/v1/api/user/{userId}/weather/today")
    Call<HomeWeatherResponse> getHomeWeather(
            @Path("userId") int userId,
            @Body HomeRequest request
             );
}
