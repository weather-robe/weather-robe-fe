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
import android.widget.LinearLayout;


public class ModalCalendarDialog extends DialogFragment {

    // 날짜 (yyyy-MM-dd)
    private String dateStr;

    // 상세조회 API 데이터
    private CalendarWeatherDTO detailData;

    // newInstance : 날짜 문자열만 전달
    public static ModalCalendarDialog newInstance(String date) {
        ModalCalendarDialog dialog = new ModalCalendarDialog();
        Bundle args = new Bundle();
        args.putString("date", date);
        dialog.setArguments(args);
        return dialog;
    }

    // CalendarFragment에서 조회한 상세데이터 주입
    public void setDetailData(CalendarWeatherDTO data) {
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
        LinearLayout keywordContainer = view.findViewById(R.id.keywordContainer);

        TextView tvKeywords = view.findViewById(R.id.tvKeywords);    // 키워드 영역
        TextView tvUserAnswer = view.findViewById(R.id.tvUserAnswer);  // 나는 ~~라고 답변했어요

        // 날짜 출력
        tvDate.setText(dateStr);

        // 상세 데이터가 존재하면 UI 구성
        if (detailData != null) {

            // 온도: 소수점은 반올림 처리
            String tempText = String.format(
                    "최고 %d° / 최저 %d°",
                    Math.round(detailData.getTemp_max()),
                    Math.round(detailData.getTemp_min())
            );
            tvTempInfo.setText(getColoredTemp(tempText));

            // 설명 텍스트
            tvCodiDesc.setText(detailData.getText());
            tvCodiDesc.setTextSize(14);

            // 코디 키워드
            keywordContainer.removeAllViews(); // 기존 뷰 제거

            if (detailData.getKeywords() != null && !detailData.getKeywords().isEmpty()) {

                for (String keyword : detailData.getKeywords()) {

                    TextView tv = new TextView(getContext());
                    tv.setText(keyword);
                    tv.setTextSize(14);
                    tv.setTextColor(Color.parseColor("#111111"));
                    tv.setBackgroundResource(R.drawable.label_bg_gary_line);
                    tv.setPadding(30, 20, 30, 20);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 12, 0);
                    tv.setLayoutParams(params);

                    keywordContainer.addView(tv);
                }

            } else {
            keywordContainer.removeAllViews();

            TextView empty = new TextView(getContext());
            empty.setText("추천 키워드가 없어요.");
            empty.setTextSize(13);
            empty.setTextColor(Color.parseColor("#B1B1B1"));

            keywordContainer.addView(empty);
        }


            String feeling = detailData.getFeeling_status();
            if (feeling != null && !feeling.isEmpty()) {
                String answerText = String.format("나는 😃%s 이라고 답변했어요.", feeling);
                tvUserAnswer.setText(answerText);
            } else {
                tvUserAnswer.setText("답변 내용이 없습니다🥲");
                tvUserAnswer.setTextSize(16);
                tvUserAnswer.setTextColor(Color.parseColor("#111111"));


            }

        } else {
            // 상세 데이터 존재X
            tvTempInfo.setText(" 날씨 정보 없음");
            tvTempInfo.setTextSize(12);
            tvTempInfo.setTextColor(Color.parseColor("#B1B1B1"));

            tvCodiDesc.setText("아직 날씨 정보가 없어서 알 수 없어요.");
            tvCodiDesc.setTextSize(12);
            tvCodiDesc.setTextColor(Color.parseColor("#B1B1B1"));

            tvKeywords.setText("-");
            tvKeywords.setTextSize(12);
            tvKeywords.setTextColor(Color.parseColor("#B1B1B1"));

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
