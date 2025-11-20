package com.cookandroid.weatherrobe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private LinearLayout tabHome, tabHourly, tabDaily, tabCalendar;
    private LinearLayout innerHome, innerHourly, innerDaily, innerCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View bottomNav = findViewById(R.id.bottom_nav);

        tabHome = bottomNav.findViewById(R.id.tab_home);
        tabHourly = bottomNav.findViewById(R.id.tab_hourly);
        tabDaily = bottomNav.findViewById(R.id.tab_daily);
        tabCalendar = bottomNav.findViewById(R.id.tab_calendar);

        innerHome = bottomNav.findViewById(R.id.inner_home);
        innerHourly = bottomNav.findViewById(R.id.inner_hourly);
        innerDaily = bottomNav.findViewById(R.id.inner_daily);
        innerCalendar = bottomNav.findViewById(R.id.inner_calendar);

        replaceFragment(new HomeFragment());
        BottomNavigation.selectTab(tabHome, innerHome, innerHourly, innerDaily, innerCalendar);

        tabHome.setOnClickListener(v -> {
            BottomNavigation.selectTab(v, innerHome, innerHourly, innerDaily, innerCalendar);
            replaceFragment(new HomeFragment());
        });

        tabHourly.setOnClickListener(v -> {
            BottomNavigation.selectTab(v, innerHome, innerHourly, innerDaily, innerCalendar);
            replaceFragment(new HourlyFragment());
        });

        tabDaily.setOnClickListener(v -> {
            BottomNavigation.selectTab(v, innerHome, innerHourly, innerDaily, innerCalendar);
            replaceFragment(new DailyFragment());
        });

        tabCalendar.setOnClickListener(v -> {
            BottomNavigation.selectTab(v, innerHome, innerHourly, innerDaily, innerCalendar);
            replaceFragment(new CalendarFragment());
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
