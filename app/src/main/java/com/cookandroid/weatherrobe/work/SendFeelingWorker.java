package com.cookandroid.weatherrobe.work;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SendFeelingWorker extends Worker {

    public SendFeelingWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        // 저장된 체감온도 값 꺼내기
        SharedPreferences prefs =
                getApplicationContext().getSharedPreferences("weather_pref", Context.MODE_PRIVATE);

        String feeling = prefs.getString("tempFeeling", "none");

        // TODO: 서버로 feeling 값을 전송하는 API 호출
        // sendFeelingToServer(feeling);

        // 로그 찍고 성공 반환
        System.out.println("23:59 체감온도 전송됨: " + feeling);

        return Result.success();
    }

    // -------------- 서버 전송 예시 --------------
    // private void sendFeelingToServer(String feeling) {
    //     // Retrofit 또는 OkHttp 사용
    // }
}
