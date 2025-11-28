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

public class DailyWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<DailyWeatherData> weatherList;
    private boolean hasHeader = false;

    public DailyWeatherAdapter(List<DailyWeatherData> weatherList, boolean hasHeader) {
        this.weatherList = weatherList;
        this.hasHeader = hasHeader;
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

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader && position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_daily_weather_card_yesterday, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_daily_weather_card, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM) {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;

            int dataPosition = position;
            if (hasHeader) {
                dataPosition = position - 1;
            }

            DailyWeatherData currentItem = weatherList.get(dataPosition);

            itemHolder.dayLabel.setText(currentItem.getDayLabel());
            itemHolder.dateValue.setText(currentItem.getDateValue());
            itemHolder.weatherIcon.setImageResource(currentItem.getWeatherIconRes());
            itemHolder.tempMin.setText(currentItem.getTempMin());
            itemHolder.tempMax.setText(currentItem.getTempMax());

        } else if (holder.getItemViewType() == TYPE_HEADER) {
        }
    }

    @Override
    public int getItemCount() {
        int headerCount = hasHeader ? 1 : 0;
        return weatherList.size() + headerCount;
    }
}