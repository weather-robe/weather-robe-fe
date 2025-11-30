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

    // 날짜 (yyyy-MM-dd)
    private String dateStr;

    // 상세조회 API 데이터
    private CalendarResDTO.CalendarDetailDTO detailData;


    // newInstance : 날짜 문자열만 전달
    public static ModalCalendarDialog newInstance(String date) {
        ModalCalendarDialog dialog = new ModalCalendarDialog();
        Bundle args = new Bundle();
        args.putString("date", date);
        dialog.setArguments(args);
        return dialog;
    }

    // CalendarFragment에서 조회한 상세데이터 주입
    public void setDetailData(CalendarResDTO.CalendarDetailDTO data) {
        this.detailData = data;
    }

    // onCreate : 날짜만 받아옴
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            dateStr = getArguments().getString("date");
        }
    }

    // onCreateView : UI 세팅
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modal_calendar, container, false);

        TextView tvDate = view.findViewById(R.id.tvDateTitle);
        TextView tvTempInfo = view.findViewById(R.id.tvTempInfo);
        TextView tvCodiDesc = view.findViewById(R.id.tvCodiDescription);
        TextView tvKeywords = view.findViewById(R.id.tvUserAnswer);

        // 날짜 출력
        tvDate.setText(dateStr);

        // 상세 데이터가 존재하면 UI 구성
        if (detailData != null) {

            // 온도
            String tempText = String.format("최고 %.1f° / 최저 %.1f°",
                    detailData.getTemp_max(),
                    detailData.getTemp_min()
            );
            tvTempInfo.setText(getColoredTemp(tempText));

            // 설명 텍스트
            tvCodiDesc.setText(detailData.getText());

            // 키워드
            if (detailData.getKeywords() != null && !detailData.getKeywords().isEmpty()) {
                tvKeywords.setText(detailData.getKeywords().toString());
            } else {
                tvKeywords.setText("추천 키워드가 없어요.");
            }

        } else {
            // 상세 데이터 존재X
            tvTempInfo.setText("날씨 정보 없음");
            tvCodiDesc.setText("데이터가 없습니다.");
            tvKeywords.setText("정보 없음");
        }

        return view;
    }

    // 숫자 색깔 적용
    private SpannableString getColoredTemp(String tempText) {
        SpannableString ss = new SpannableString(tempText);

        // 최고 온도
        int highStart = tempText.indexOf(" ") + 1;
        int highEnd = tempText.indexOf("°") + 1;

        if (highStart >= 0 && highEnd > highStart) {
            ss.setSpan(
                    new ForegroundColorSpan(Color.parseColor("#E50000")),
                    highStart, highEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        // 최저 온도
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

    // 다이얼로그 모양 재조정
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {

            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            getDialog().getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT)
            );
        }
    }
}
