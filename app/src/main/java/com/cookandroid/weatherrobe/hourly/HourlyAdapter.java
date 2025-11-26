package com.cookandroid.weatherrobe.hourly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        holder.time.setText(item.time);
        holder.icon.setImageResource(item.iconRes);
        holder.temp.setText(item.temp);
        holder.feel.setText(item.feel);
        holder.rainProb.setText(item.rainProb);
        holder.rainAmount.setText(item.rainAmount);
        holder.humidity.setText(item.humidity);
        holder.wind.setText(item.wind);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView time, temp, feel, rainProb, rainAmount, humidity, wind;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.txt_time);
            icon = itemView.findViewById(R.id.img_weather);
            temp = itemView.findViewById(R.id.txt_temp);
            feel = itemView.findViewById(R.id.txt_feel);
            rainProb = itemView.findViewById(R.id.txt_rainProb);
            rainAmount = itemView.findViewById(R.id.txt_rainAmount);
            humidity = itemView.findViewById(R.id.txt_humidity);
            wind = itemView.findViewById(R.id.txt_wind);
        }
    }
}
