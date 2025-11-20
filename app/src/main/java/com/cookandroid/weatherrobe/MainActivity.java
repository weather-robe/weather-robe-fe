package com.cookandroid.weatherrobe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View bottomNav = findViewById(R.id.bottom_nav);

        BottomNavigation.setup(bottomNav, tabId -> {
            if (tabId == R.id.tab_home) {
                replaceFragment(new HomeFragment());
            } else if (tabId == R.id.tab_hourly) {
                replaceFragment(new HourlyFragment());
            } else if (tabId == R.id.tab_daily) {
                replaceFragment(new DailyFragment());
            } else if (tabId == R.id.tab_calendar) {
                replaceFragment(new CalendarFragment());
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
