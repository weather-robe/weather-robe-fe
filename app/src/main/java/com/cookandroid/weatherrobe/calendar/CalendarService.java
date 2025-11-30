package com.cookandroid.weatherrobe.calendar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import retrofit2.http.Query;

import com.cookandroid.weatherrobe.common.CommonApiResponse;

public interface CalendarService {

    // 캘린더 전체 조회
    @GET("/v1/api/user/{userId}/calender/")
    Call<CommonApiResponse<List<CalendarResDTO.CalendarItem>>> getCalendarList(
            @Path("userId") int userId
    );

    // 캘린더 상세 조회
    @GET("/v1/api/user/{userId}/calender/detail")
    Call<CommonApiResponse<CalendarResDTO.CalendarDetailDTO>> getCalendarDetail(
            @Path("userId") int userId,
            @Query("date") String date
    );
}