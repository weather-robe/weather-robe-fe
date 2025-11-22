package com.cookandroid.weatherrobe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private View headerLayout;
    private ImageView headerLeftIcon;
    private ImageView headerMapIcon;
    private TextView headerTitle;
    private ImageView headerRightIcon;
    private View headerBottomBorder;

    private ImageView customStatusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_main);

        customStatusBar = findViewById(R.id.custom_status_bar);

        headerLayout = findViewById(R.id.header_root);
        headerLeftIcon = findViewById(R.id.header_left_icon);
        headerMapIcon = findViewById(R.id.header_map_icon);
        headerTitle = findViewById(R.id.header_title);
        headerRightIcon = findViewById(R.id.header_right_icon);
        headerBottomBorder = findViewById(R.id.header_bottom_border);

        View bottomNav = findViewById(R.id.bottom_nav);

        replaceFragment(new HomeFragment());
        setHeaderStyle(true);
        setStatusBarImage(true);
        headerLayout.setVisibility(View.VISIBLE);

        BottomNavigation.setup(bottomNav, tabId -> {
            if (tabId == R.id.tab_home) {
                headerLayout.setVisibility(View.VISIBLE);
                replaceFragment(new HomeFragment());

                setHeaderStyle(true);
                setStatusBarImage(true);

            } else if (tabId == R.id.tab_hourly) {
                headerLayout.setVisibility(View.VISIBLE);
                replaceFragment(new HourlyFragment());

                setHeaderStyle(false);
                setStatusBarImage(false);

            } else if (tabId == R.id.tab_daily) {
                headerLayout.setVisibility(View.VISIBLE);
                replaceFragment(new DailyFragment());

                setHeaderStyle(false);
                setStatusBarImage(false);

            } else if (tabId == R.id.tab_calendar) {
                headerLayout.setVisibility(View.GONE);
                replaceFragment(new CalendarFragment());

                setStatusBarImage(false);
            }
        });
    }

    private void setStatusBarImage(boolean isHome) {
        if (isHome) {
            customStatusBar.setImageResource(R.drawable.ic_menu_white);
            customStatusBar.setVisibility(View.VISIBLE);
        } else {
            customStatusBar.setImageResource(R.drawable.ic_menu_black);
            customStatusBar.setVisibility(View.VISIBLE);
        }
    }

    private void setHeaderStyle(boolean isHome) {
        if (headerLayout == null ||
                headerTitle == null ||
                headerLeftIcon == null ||
                headerMapIcon == null ||
                headerRightIcon == null ||
                headerBottomBorder == null) {
            return;
        }

        if (isHome) {
            headerLayout.setBackgroundColor(0x00000000);
            headerTitle.setTextColor(0xFFFFFFFF);
            headerLeftIcon.setColorFilter(0xFFFFFFFF);
            headerMapIcon.setColorFilter(0xFFFFFFFF);
            headerRightIcon.setColorFilter(0xFFFFFFFF);
            headerBottomBorder.setVisibility(View.GONE);
        } else {
            headerLayout.setBackgroundColor(0xFFFFFFFF);
            headerTitle.setTextColor(0xFF111111);
            headerLeftIcon.setColorFilter(0xFF111111);
            headerMapIcon.setColorFilter(0xFF111111);
            headerRightIcon.setColorFilter(0xFF111111);
            headerBottomBorder.setVisibility(View.VISIBLE);
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
