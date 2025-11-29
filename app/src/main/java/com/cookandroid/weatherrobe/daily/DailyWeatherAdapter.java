package com.cookandroid.weatherrobe.daily;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.weatherrobe.R;
import com.facebook.shimmer.ShimmerFrameLayout; // Shimmer 라이브러리 추가

import java.util.List;

public class DailyWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DailyWeatherData> weatherList;
    private boolean isYesterday;
    private boolean isShimmering = true;
    private static final int SHIMMER_ITEM_COUNT = 9;
    private static final int VIEW_TYPE_DATA_YESTERDAY = 0;
    private static final int VIEW_TYPE_DATA_NORMAL = 1;
    private static final int VIEW_TYPE_SHIMMER = 2;

    public DailyWeatherAdapter(List<DailyWeatherData> weatherList, boolean isYesterday) {
        this.weatherList = weatherList;
        this.isYesterday = isYesterday;
    }
    public static class DataViewHolder extends RecyclerView.ViewHolder {
        public TextView dayLabel;
        public TextView dateValue;
        public ImageView weatherIcon;
        public TextView tempMin;
        public TextView tempMax;

        public DataViewHolder(View itemView) {
            super(itemView);
            dayLabel = itemView.findViewById(R.id.text_day_label);
            dateValue = itemView.findViewById(R.id.text_date_value);
            weatherIcon = itemView.findViewById(R.id.image_weather_icon);
            tempMin = itemView.findViewById(R.id.text_temp_min);
            tempMax = itemView.findViewById(R.id.text_temp_max);
        }
    }

    public static class ShimmerViewHolder extends RecyclerView.ViewHolder {
        public ShimmerFrameLayout shimmerLayout;

        public ShimmerViewHolder(View itemView) {
            super(itemView);
            shimmerLayout = (ShimmerFrameLayout) itemView;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShimmering) {
            return VIEW_TYPE_SHIMMER;
        }
        if (position == 0 && isYesterday) {
            return VIEW_TYPE_DATA_YESTERDAY;
        } else {
            return VIEW_TYPE_DATA_NORMAL;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_SHIMMER) {
            View shimmerView = inflater.inflate(R.layout.item_daily_weather_simmer_card, parent, false);
            return new ShimmerViewHolder(shimmerView);
        }
        if (viewType == VIEW_TYPE_DATA_YESTERDAY) {
            View view = inflater.inflate(R.layout.item_daily_weather_card_yesterday, parent, false);
            return new DataViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_daily_weather_card, parent, false);
            return new DataViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_SHIMMER) {
            ((ShimmerViewHolder) holder).shimmerLayout.startShimmer();
            return;
        }

        DataViewHolder dataHolder = (DataViewHolder) holder;
        DailyWeatherData currentItem = weatherList.get(position);

        dataHolder.dayLabel.setText(currentItem.getDayLabel());
        dataHolder.dateValue.setText(currentItem.getDateValue());

        if(holder.getItemViewType() == VIEW_TYPE_DATA_NORMAL) {
            dataHolder.weatherIcon.setImageResource(currentItem.getWeatherIconRes());
        }

        dataHolder.tempMin.setText(currentItem.getTempMin());
        dataHolder.tempMax.setText(currentItem.getTempMax());
    }

    @Override
    public int getItemCount() {
        return isShimmering ? SHIMMER_ITEM_COUNT : weatherList.size();
    }

    public void setShimmering(boolean isShimmering) {
        this.isShimmering = isShimmering;
        notifyDataSetChanged();
    }
    public void setHasYesterday(boolean hasYesterday) {
        this.isYesterday = hasYesterday;
    }
}