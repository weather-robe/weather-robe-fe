package com.cookandroid.weatherrobe;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class BottomNavigation {

    public interface OnTabSelectedListener {
        void onTabSelected(int tabId);
    }

    private static int dp(int value) {
        return (int) (value * Resources.getSystem().getDisplayMetrics().density);
    }

    public static void setup(
            View bottomNav,
            OnTabSelectedListener listener
    ) {
        // tab_xxx 는 ConstraintLayout이므로 LinearLayout 캐스팅 금지
        ViewGroup tabHome = bottomNav.findViewById(R.id.tab_home);
        ViewGroup tabHourly = bottomNav.findViewById(R.id.tab_hourly);
        ViewGroup tabDaily = bottomNav.findViewById(R.id.tab_daily);
        ViewGroup tabCalendar = bottomNav.findViewById(R.id.tab_calendar);

        // inner_xxx 는 여전히 LinearLayout
        LinearLayout innerHome = bottomNav.findViewById(R.id.inner_home);
        LinearLayout innerHourly = bottomNav.findViewById(R.id.inner_hourly);
        LinearLayout innerDaily = bottomNav.findViewById(R.id.inner_daily);
        LinearLayout innerCalendar = bottomNav.findViewById(R.id.inner_calendar);

        Runnable reset = () -> {
            innerHome.setBackgroundResource(0);
            innerHourly.setBackgroundResource(0);
            innerDaily.setBackgroundResource(0);
            innerCalendar.setBackgroundResource(0);
        };

        View.OnClickListener click = v -> {
            reset.run();

            if (v.getId() == R.id.tab_home) {
                innerHome.setBackgroundResource(R.drawable.bg_selected_tab);
            } else if (v.getId() == R.id.tab_hourly) {
                innerHourly.setBackgroundResource(R.drawable.bg_selected_tab);
            } else if (v.getId() == R.id.tab_daily) {
                innerDaily.setBackgroundResource(R.drawable.bg_selected_tab);
            } else if (v.getId() == R.id.tab_calendar) {
                innerCalendar.setBackgroundResource(R.drawable.bg_selected_tab);
            }

            listener.onTabSelected(v.getId());
        };

        tabHome.setOnClickListener(click);
        tabHourly.setOnClickListener(click);
        tabDaily.setOnClickListener(click);
        tabCalendar.setOnClickListener(click);

        // 디폴트 Home 선택
        click.onClick(tabHome);
    }
}
