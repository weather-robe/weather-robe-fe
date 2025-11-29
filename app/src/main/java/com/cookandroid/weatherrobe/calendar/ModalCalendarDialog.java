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

        LinearLayout layoutWeather = view.findViewById(R.id.layoutWeather);

        String key = year + "-" + (month + 1) + "-" + day;

        tvDate.setText((month + 1) + "월 " + day + "일 날씨");

        // --- 날씨 ---
        if (CalendarDataStore.weatherMap.containsKey(key)) {
            tvTempInfo.setText(CalendarDataStore.weatherMap.get(key));
            layoutWeather.setVisibility(View.VISIBLE);
        } else {
            layoutWeather.setVisibility(View.GONE);
        }

        // --- 코디 텍스트 ---
        if (CalendarDataStore.weatherMap.containsKey(key)) {
            tvCodiDesc.setText("오늘은 어제보다 추워졌고, 날씨는 전반적으로 흐려요.\n일교차가 크니 조심하세요.");
        } else {
            tvCodiDesc.setText("아직 날씨 정보가 없어요!");
        }

        // --- 사용자 답변 ---
        if (CalendarDataStore.answerMap.containsKey(key)) {
            tvUserAnswer.setText("나는 " + CalendarDataStore.answerMap.get(key) + " 이라고 답변했어요.");
        } else {
            tvUserAnswer.setText("답변하지 않았어요🥲");
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
}
