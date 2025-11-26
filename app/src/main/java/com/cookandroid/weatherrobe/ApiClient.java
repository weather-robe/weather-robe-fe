// Retrofit 객체를 생성하는 싱글톤 클래스

package com.cookandroid.weatherrobe;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")  // AVD 전용 host 접근 IP
                    .addConverterFactory(GsonConverterFactory.create())  // JSON-> Java 변환 설정
                    .build();
        }
        return retrofit;
    }
}
