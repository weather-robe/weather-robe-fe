package com.cookandroid.weatherrobe.Home;

import com.cookandroid.weatherrobe.Home.dto.HomeReqDTO;
import com.cookandroid.weatherrobe.Home.dto.HomeResDTO;
import com.cookandroid.weatherrobe.common.CommonApiResponse;

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
    @POST("/v1/api/user/{userId}/weather/{weatherId}/keyword")
    Call<CommonApiResponse<HomeResDTO.PostKeywordDTO>> getWeatherKeywords(
            @Path("userId") int userId,
            @Path("weatherId") int weatherId
    );
    @POST("/v1/api/user/{userId}/weather/{weatherId}/image")
    Call<CommonApiResponse<HomeResDTO.PostImageDTO>> getWeatherImages(
            @Path("userId") int userId,
            @Path("weatherId") int weatherId
    );
    @POST("/v1/api/user/{userId}/weather/{weatherId}")
    Call<CommonApiResponse<HomeResDTO.PostFeedbackDTO>> postFeedback(
            @Path("userId") int userId,
            @Path("weatherId") int weatherId,
            @Body HomeReqDTO.PostHomeDTO dto
    );
}
