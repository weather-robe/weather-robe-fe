// Retrofit 객체를 생성하는 싱글톤 클래스

package com.cookandroid.weatherrobe;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.2:8080/")  // 일단 캘린더 API는 이설하 로컬 서버로 돌리는 중
                    .addConverterFactory(GsonConverterFactory.create())  // JSON-> Java 변환 설정
                    .build();
        }
        return retrofit;
    }
}
