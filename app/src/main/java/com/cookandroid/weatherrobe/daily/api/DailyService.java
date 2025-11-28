package com.cookandroid.weatherrobe.daily.api;

import com.cookandroid.weatherrobe.common.CommonApiResponse;
import com.cookandroid.weatherrobe.daily.dto.DailyReqDTO;
import com.cookandroid.weatherrobe.daily.dto.DailyResDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DailyService {
    @POST("/v1/api/user/{userId}/weather/daily")
    Call<CommonApiResponse<DailyResDTO.PostDailyDTO>> getWeatherForecast(@Path("userId") int userId, @Body DailyReqDTO.PostDailyDTO dto);
}
