// GridView 표시용 어댑터

package com.cookandroid.weatherrobe.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cookandroid.weatherrobe.R;

import java.util.List;

public class CalendarAdapter extends BaseAdapter {

    private final Context context;
    private final List<DayItem> days;

    public CalendarAdapter(Context context, List<DayItem> days) {
        this.context = context;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_calendar_day, parent, false);

        TextView tvDay = view.findViewById(R.id.tvDay);

        DayItem item = days.get(position);

        if (!item.isDay) {
            // 빈칸
            tvDay.setText("");
        } else {
            tvDay.setText(item.dayText);

            if (item.isToday) {
                tvDay.setTextColor(Color.parseColor("#3C6FF4")); // 오늘 날짜 파란색
            } else {
                tvDay.setTextColor(Color.parseColor("#222222"));
            }
        }

        return view;
    }
}

