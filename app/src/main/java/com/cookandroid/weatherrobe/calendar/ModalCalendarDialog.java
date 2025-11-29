package com.cookandroid.weatherrobe.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.cookandroid.weatherrobe.R;

public class ModalCalendarDialog extends DialogFragment {

    private int year, month, day;


    public static ModalCalendarDialog newInstance(int y, int m, int d) {
        ModalCalendarDialog dialog = new ModalCalendarDialog();
        Bundle args = new Bundle();
        args.putInt("y", y);
        args.putInt("m", m);
        args.putInt("d", d);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        year = getArguments().getInt("y");
        month = getArguments().getInt("m");
        day = getArguments().getInt("d");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modal_calendar, container, false);

        TextView tvDate = view.findViewById(R.id.tvDateTitle);
        TextView tvTempInfo = view.findViewById(R.id.tvTempInfo);
        TextView tvCodiDesc = view.findViewById(R.id.tvCodiDescription);
        TextView tvUserAnswer = view.findViewById(R.id.tvUserAnswer);

        // yyyy-MM-dd 형식
        String key = String.format("%04d-%02d-%02d", year, month + 1, day);

        // 날짜 출력
        tvDate.setText((month + 1) + "월 " + day + "일");

        // 온도 텍스트
        String tempText;

        if (CalendarDataStore.weatherMap.containsKey(key)) {
            tempText = CalendarDataStore.weatherMap.get(key); // ex: "최고 16° / 최저 1°"
        } else {
            tempText = "최고 -° / 최저 -°"; // 기본 텍스트
        }

        // 숫자 부분(16°, 1° 또는 -°)만 색상 적용
        tvTempInfo.setText(getColoredTemp(tempText));

        // 코디 텍스트
        if (CalendarDataStore.weatherMap.containsKey(key)) {
            tvCodiDesc.setText("오늘은 어제보다 추워졌고, 날씨는 전반적으로 흐려요.\n일교차가 크니 조심하세요.");
        } else {
            tvCodiDesc.setText("아직 날씨 정보가 없어요!");
        }

        // 사용자 답변
        if (CalendarDataStore.answerMap.containsKey(key)) {
            tvUserAnswer.setText("나는 " + CalendarDataStore.answerMap.get(key) + " 이라고 답변했어요.");
        } else {
            tvUserAnswer.setText("답변하지 않았어요🥲");
        }

        return view;
    }

    // 숫자 부분만 색칠하는 함수
    private SpannableString getColoredTemp(String tempText) {
        SpannableString ss = new SpannableString(tempText);

        // 최고 온도 숫자 부분
        int highStart = tempText.indexOf(" ") + 1;             // 숫자 시작
        int highEnd = tempText.indexOf("°") + 1;               // ° 포함

        if (highStart >= 0 && highEnd > highStart) {
            ss.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#E50000")),
                    highStart, highEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        // 최저 온도 숫자 부분
        int lowStart = tempText.lastIndexOf(" ") + 1;
        int lowEnd = tempText.lastIndexOf("°") + 1;

        if (lowStart >= 0 && lowEnd > lowStart) {
            ss.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#0031E3")),
                    lowStart, lowEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        return ss;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {

            // 모달 크기
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            // 모달 배경 투명 처리 (라운드 드러남)
            getDialog().getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT)
            );
        }
    }
}
