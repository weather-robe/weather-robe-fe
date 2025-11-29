package com.cookandroid.weatherrobe.work;

import android.content.Context;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

// 앱에서 매일 23:59에 자동으로 '사용자 실제 체감온도'를
// 서버로 전송하는 작업을 WorkManager에 등록함
public class FeelingWorkManager {

    // 매일 보내는 작업의 이름
    private static final String WORK_NAME = "sendFeelingWork";

    // 스케쥴 등록 함수: 매일 23:59 실행 작업 예약
    public static void scheduleFeelingWork(Context context) {

        long delay = calculateDelayTo2359();  // 남은 실행 시간 계산

        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(SendFeelingWorker.class, 1, TimeUnit.DAYS)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .build();

        // 매일 반복되는 작업을 등록
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,  // 이미 있으면 갱신
                request
        );
    }

    // 오늘 또는 내일의 23:59까지 남은 시간 계산
    private static long calculateDelayTo2359() {
        Calendar now = Calendar.getInstance();
        Calendar target = Calendar.getInstance();

        target.set(Calendar.HOUR_OF_DAY, 23);
        target.set(Calendar.MINUTE, 59);
        target.set(Calendar.SECOND, 0);
        target.set(Calendar.MILLISECOND, 0);

        // 이미 23:59 지나면 내일로
        if (now.after(target)) {
            target.add(Calendar.DAY_OF_YEAR, 1);
        }

        return target.getTimeInMillis() - now.getTimeInMillis();
    }
}
