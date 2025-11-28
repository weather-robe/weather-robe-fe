package com.cookandroid.weatherrobe.daily;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.weatherrobe.R;

import java.util.List;

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.ItemViewHolder> {

    private List<DailyWeatherData> weatherList;
    private boolean isYesterday;

    public DailyWeatherAdapter(List<DailyWeatherData> weatherList, boolean isYesterday) {
        this.weatherList = weatherList;
        this.isYesterday = isYesterday;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView dayLabel;
        public TextView dateValue;
        public ImageView weatherIcon;
        public TextView tempMin;
        public TextView tempMax;

        public ItemViewHolder(View itemView) {
            super(itemView);
            dayLabel = itemView.findViewById(R.id.text_day_label);
            dateValue = itemView.findViewById(R.id.text_date_value);
            weatherIcon = itemView.findViewById(R.id.image_weather_icon);
            tempMin = itemView.findViewById(R.id.text_temp_min);
            tempMax = itemView.findViewById(R.id.text_temp_max);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == 0 && isYesterday) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_daily_weather_card_yesterday, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_daily_weather_card, parent, false);
        }

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        DailyWeatherData currentItem = weatherList.get(position);

        holder.dayLabel.setText(currentItem.getDayLabel());
        holder.dateValue.setText(currentItem.getDateValue());
        if(!(position == 0 && isYesterday)) holder.weatherIcon.setImageResource(currentItem.getWeatherIconRes());
        holder.tempMin.setText(currentItem.getTempMin());
        holder.tempMax.setText(currentItem.getTempMax());
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }
}