package com.cookandroid.weatherrobe;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/calendar/month")   // 서버 엔드포인트 주소 넣기
    Call<MonthResponse> getMonthInfo();
}
