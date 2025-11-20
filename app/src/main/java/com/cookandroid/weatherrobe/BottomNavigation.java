package com.cookandroid.weatherrobe;

import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;

public class BottomNavigation {

    private static int dp(int value) {
        return (int) (value * Resources.getSystem().getDisplayMetrics().density);
    }

    public static void resetTabs(
            LinearLayout innerHome,
            LinearLayout innerHourly,
            LinearLayout innerDaily,
            LinearLayout innerCalendar
    ) {
        innerHome.setBackgroundResource(0);
        innerHourly.setBackgroundResource(0);
        innerDaily.setBackgroundResource(0);
        innerCalendar.setBackgroundResource(0);
    }

    public static void selectTab(
            View selected,
            LinearLayout innerHome,
            LinearLayout innerHourly,
            LinearLayout innerDaily,
            LinearLayout innerCalendar
    ) {
        resetTabs(innerHome, innerHourly, innerDaily, innerCalendar);

        int id = selected.getId();

        if (id == R.id.tab_home) {
            innerHome.setBackgroundResource(R.drawable.bg_selected_tab);
        } else if (id == R.id.tab_hourly) {
            innerHourly.setBackgroundResource(R.drawable.bg_selected_tab);
        } else if (id == R.id.tab_daily) {
            innerDaily.setBackgroundResource(R.drawable.bg_selected_tab);
        } else if (id == R.id.tab_calendar) {
            innerCalendar.setBackgroundResource(R.drawable.bg_selected_tab);
        }
    }
}
