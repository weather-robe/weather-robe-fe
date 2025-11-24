package com.cookandroid.weatherrobe;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient{
    private static Retro retrofit = null;

    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.2:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
        }
    }
}
