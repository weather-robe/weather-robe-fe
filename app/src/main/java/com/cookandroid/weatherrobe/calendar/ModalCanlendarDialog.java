package com.cookandroid.weatherrobe.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cookandroid.weatherrobe.R;

import java.util.Calendar;
import java.util.Map;

public class ModalCalendarDialog extends DialogFragment {

    private int year, month, day;

    // 외부에서 주입받는 데이터
    private Map<String, String> weatherMap;     // key: "2025-11-15", value: "최고 16° / 최저 1°"
    private Map<String, String> answerMap;      // key: "2025-11-15", value: "😃적당"

    public ModalCalendarDialog(int year, int month, int day,
                               Map<String, String> weatherMap,
                               Map<String, String> answerMap) {

        this.year = year;
        this.month = month;
        this.day = day;

        this.weatherMap = weatherMap;
        this.answerMap = answerMap;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modal_calendar, container, false);

        // UI Bind
        TextView tvDate = view.findViewById(R.id.tvDateTitle);
        TextView tvTempInfo = view.findViewById(R.id.tvTempInfo);
        TextView tvCodiDesc = view.findViewById(R.id.tvCodiDescription);
        TextView tvUserAnswer = view.findViewById(R.id.tvUserAnswer);

        LinearLayout layoutWeather = view.findViewById(R.id.layoutWeather);
        LinearLayout layoutCodi = view.findViewById(R.id.layoutCodi);

        String key = year + "-" + (month + 1) + "-" + day;

        // 날짜 제목
        tvDate.setText((month + 1) + "월 " + day + "일 날씨");


        // --------------- 콘텐츠 1: 날씨 여부 ---------------
        if (weatherMap.containsKey(key)) {
            String weatherText = weatherMap.get(key);
            tvTempInfo.setText(weatherText);
            layoutWeather.setVisibility(View.VISIBLE);
        } else {
            layoutWeather.setVisibility(View.GONE);
        }


        // --------------- 콘텐츠 2: 코디 키워드 ---------------
        if (weatherMap.containsKey(key)) {
            tvCodiDesc.setText("오늘은 어제보다 추워졌고, 날씨는 전반적으로 흐려요.\n또한 일교차가 커서 감기에 유의하셔야 해요.");
        } else {
            tvCodiDesc.setText("아직 날씨 정보가 없어요!");
        }


        // --------------- 콘텐츠 3: 사용자 답변 ---------------
        if (answerMap.containsKey(key)) {
            String ans = answerMap.get(key);
            tvUserAnswer.setText("나는 " + ans + " 이라고 답변했어요.");
        } else {
            tvUserAnswer.setText("답변하지 않았어요🥲");
        }

        return view;
    }
}
