package com.cookandroid.weatherrobe.hourly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.weatherrobe.R;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {

    private ArrayList<HourlyItem> items;

    public HourlyAdapter(ArrayList<HourlyItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_hour, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyItem item = items.get(position);

        String hour = item.time.split(":")[0] + "시";
        holder.time.setText(hour);

        switch (item.weather) {
            case "Clouds":
                holder.icon.setImageResource(R.drawable.ic_weather_cloudy);
                break;
            case "Rain":
                holder.icon.setImageResource(R.drawable.ic_weather_rainy);
            case "Clear":
                holder.icon.setImageResource(R.drawable.ic_weather_sunny);
        }

        holder.temp.setText(String.format("%.1f°", item.temp));
        holder.feel.setText(String.format("%.1f°", item.feels_like));
        holder.rainProb.setText((int) (item.pop * 100) + "%");
        holder.rainAmount.setText(item.rain == null ? "0mm" : item.rain + "mm");
        holder.humidity.setText(item.humidity + "%");
        holder.windText.setText(item.wind_text);
        holder.wind.setText(String.format("%.1fm/s", item.wind_speed));

        holder.rowFeel.setBackgroundColor(0xFFF8F8F8);
        holder.rowRainAmount.setBackgroundColor(0xFFF8F8F8);
        holder.rowWind.setBackgroundColor(0xFFF8F8F8);

        if (position == 0) {
            holder.blueOverlay.setVisibility(View.VISIBLE);
        } else {
            holder.blueOverlay.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView time, temp, feel, rainProb, rainAmount, humidity, windText, wind;
        ImageView icon;
        LinearLayout rowFeel, rowRainAmount, rowWind;
        View blueOverlay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.txt_time);
            icon = itemView.findViewById(R.id.img_weather);
            temp = itemView.findViewById(R.id.txt_temp);
            feel = itemView.findViewById(R.id.txt_feel);
            rainProb = itemView.findViewById(R.id.txt_rainProb);
            rainAmount = itemView.findViewById(R.id.txt_rainAmount);
            humidity = itemView.findViewById(R.id.txt_humidity);
            windText = itemView.findViewById(R.id.txt_wind);
            wind = itemView.findViewById(R.id.txt_wind_speed);

            rowFeel = itemView.findViewById(R.id.row_feel);
            rowRainAmount = itemView.findViewById(R.id.row_rainAmount);
            rowWind = itemView.findViewById(R.id.row_wind);

            blueOverlay = itemView.findViewById(R.id.blue_overlay);
        }
    }
}
