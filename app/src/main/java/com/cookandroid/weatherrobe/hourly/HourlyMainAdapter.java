package com.cookandroid.weatherrobe.hourly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.weatherrobe.R;

import java.util.ArrayList;

public class HourlyMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_WEATHER_TABLE = 0;
    private static final int TYPE_PM_CARDS = 1;

    private String dateText = "";
    private String dayText = "";
    private ArrayList<HourlyItem> hourlyList = new ArrayList<>();
    private HourlyAdapter hourlyAdapter;

    // PM 데이터
    private String pm10text = "";
    private int pm10 = 0;
    private String pm25text = "";
    private int pm25 = 0;

    public HourlyMainAdapter() {
        this.hourlyAdapter = new HourlyAdapter(hourlyList);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_WEATHER_TABLE : TYPE_PM_CARDS;
    }

    @Override
    public int getItemCount() {
        return 2; // Weather Table + PM Cards
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_WEATHER_TABLE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_hourly_weather_table, parent, false);
            return new WeatherTableViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_hourly_pm_cards, parent, false);
            return new PmCardsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WeatherTableViewHolder) {
            ((WeatherTableViewHolder) holder).bind();
        } else if (holder instanceof PmCardsViewHolder) {
            ((PmCardsViewHolder) holder).bind();
        }
    }

    // 데이터 업데이트 메서드
    public void setDateInfo(String date, String day) {
        this.dateText = date;
        this.dayText = day;
    }

    public void setHourlyData(ArrayList<HourlyItem> hourlyList) {
        this.hourlyList.clear();
        this.hourlyList.addAll(hourlyList);
        hourlyAdapter.notifyDataSetChanged();
    }

    public void setPmData(String pm10text, int pm10, String pm25text, int pm25) {
        this.pm10text = pm10text;
        this.pm10 = pm10;
        this.pm25text = pm25text;
        this.pm25 = pm25;
    }

    // WeatherTableViewHolder
    class WeatherTableViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtDay;
        RecyclerView recyclerWeather;

        public WeatherTableViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtDay = itemView.findViewById(R.id.txt_day);
            recyclerWeather = itemView.findViewById(R.id.recycler_weather);

            // 가로 RecyclerView 설정
            recyclerWeather.setLayoutManager(
                    new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false)
            );
            recyclerWeather.setAdapter(hourlyAdapter);
        }

        public void bind() {
            txtDate.setText(dateText);
            txtDay.setText(dayText);
        }
    }

    // PmCardsViewHolder
    class PmCardsViewHolder extends RecyclerView.ViewHolder {
        ImageView pm10Icon, pm25Icon;
        TextView pm10Value, pm25Value;
        TextView pm10Comment2, pm25Comment2;
        TextView pm10Comment3, pm25Comment3;
        View pm10BarFill, pm25BarFill;
        TextView pm10Title, pm25Title;

        public PmCardsViewHolder(@NonNull View itemView) {
            super(itemView);

            View pm10Card = itemView.findViewById(R.id.pm10_card);
            View pm25Card = itemView.findViewById(R.id.pm25_card);

            pm10Icon = pm10Card.findViewById(R.id.pm_icon);
            pm25Icon = pm25Card.findViewById(R.id.pm_icon);

            pm10Value = pm10Card.findViewById(R.id.pm_status);
            pm25Value = pm25Card.findViewById(R.id.pm_status);

            pm10BarFill = pm10Card.findViewById(R.id.pm_bar_fill);
            pm25BarFill = pm25Card.findViewById(R.id.pm_bar_fill);

            pm10Comment2 = pm10Card.findViewById(R.id.pm_comment2);
            pm25Comment2 = pm25Card.findViewById(R.id.pm_comment2);

            pm10Comment3 = pm10Card.findViewById(R.id.pm_comment3);
            pm25Comment3 = pm25Card.findViewById(R.id.pm_comment3);

            pm10Title = pm10Card.findViewById(R.id.pm_title);
            pm25Title = pm25Card.findViewById(R.id.pm_title);

            pm10Title.setText("미세먼지");
            pm25Title.setText("초미세먼지");
        }

        public void bind() {
            updatePmCard(pm10Icon, pm10Value, pm10text, pm10,
                    pm10BarFill, pm10Comment2, pm10Comment3, true);

            updatePmCard(pm25Icon, pm25Value, pm25text, pm25,
                    pm25BarFill, pm25Comment2, pm25Comment3, false);
        }

        private void updatePmCard(ImageView icon, TextView valueText, String gradeText,
                                   int value, View barFill, TextView comment2,
                                   TextView comment3, boolean isPm10) {

            valueText.setText(gradeText + " " + value + "㎍/㎥");
            icon.setImageResource(getPmIcon(gradeText));

            int goodLimit = isPm10 ? 30 : 15;
            comment2.setText(value <= goodLimit ? "이하" : "이상");

            comment3.setText(getActivityMessage(gradeText));

            barFill.setBackgroundColor(getBarColor(gradeText));
            updateBarWidth(barFill, value);
        }

        private String getActivityMessage(String grade) {
            switch (grade) {
                case "좋음": return "야외 활동에 적합";
                case "보통": return "몸상태에 따라 활동 유의";
                case "나쁨": return "가급적 실내 활동 권장";
                case "매우 나쁨": return "실외 활동 제한 및 마스크 착용 권장";
                default: return "활동 유의";
            }
        }

        private int getBarColor(String grade) {
            switch (grade) {
                case "좋음": return 0xFF8FDA92;
                case "보통": return 0xFFFFED65;
                case "나쁨": return 0xFFFF9800;
                case "매우 나쁨": return 0xFFFD675C;
                default: return 0xFF8FDA92;
            }
        }

        private void updateBarWidth(View fill, int value) {
            fill.post(() -> {
                int parentWidth = ((View) fill.getParent()).getWidth();
                float ratio = Math.min(value, 100) / 100f;
                ViewGroup.LayoutParams params = fill.getLayoutParams();
                params.width = (int) (parentWidth * ratio);
                fill.setLayoutParams(params);
            });
        }

        private int getPmIcon(String grade) {
            switch (grade) {
                case "좋음": return R.drawable.ic_pm_good;
                case "보통": return R.drawable.ic_pm_normal;
                case "나쁨": return R.drawable.ic_pm_bad;
                case "매우 나쁨": return R.drawable.ic_pm_very_bad;
                default: return R.drawable.ic_pm_normal;
            }
        }
    }
}
