package com.cookandroid.weatherrobe.login;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static LoginService loginService;

    public static LoginService getLoginService() {
        if (loginService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.weather-robe.kro.kr")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            loginService = retrofit.create(LoginService.class);
        }
        return loginService;
    }
}
