package com.cookandroid.weatherrobe.work;

import android.content.Context;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class FeelingWorkManager {

    // 매일 보내는 작업의 이름
    private static final String WORK_NAME = "sendFeelingWork";

    public static void scheduleFeelingWork(Context context) {

        long delay = calculateDelayTo2359();

        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(SendFeelingWorker.class, 1, TimeUnit.DAYS)
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .build();

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
