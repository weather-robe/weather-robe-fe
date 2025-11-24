package com.cookandroid.weatherrobe;

import retrofit.Call;
import retrofit2.http.GET;

public class ApiService {
    @GET("/calendar/month")
    Call<MonthResponse> getMonthInfo();
}
