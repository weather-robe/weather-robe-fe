package com.cookandroid.weatherrobe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import com.cookandroid.weatherrobe.Home.HomeFragment;
import com.cookandroid.weatherrobe.hourly.HourlyFragment;
import com.cookandroid.weatherrobe.daily.DailyFragment;
import com.cookandroid.weatherrobe.calendar.CalendarFragment;
import com.cookandroid.weatherrobe.location.AppLocationManager;
import com.cookandroid.weatherrobe.location.LocationUpdateListener;
import com.cookandroid.weatherrobe.login.LoginActivity;
import com.cookandroid.weatherrobe.Home.model.SharedWeatherViewModel;


public class MainActivity extends AppCompatActivity implements LocationUpdateListener {

    private View headerLayout;
    private ImageView headerLeftIcon;
    private ImageView headerMapIcon;
    private TextView headerTitle;
    private ImageView headerRightIcon;
    private View headerBottomBorder;

    private AppLocationManager locationManager;
    private SharedWeatherViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sharedViewModel = new ViewModelProvider(this).get(SharedWeatherViewModel.class);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ImageView closeBtn = findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(v -> drawer.closeDrawer(GravityCompat.START));

        LinearLayout logoutBtn = findViewById(R.id.menu_logout);
        logoutBtn.setOnClickListener(v -> {

            SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
            prefs.edit().clear().apply();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            finish();
        });

        ImageView menuButton = findViewById(R.id.header_left_icon);

        menuButton.setOnClickListener(v -> {
            drawer.openDrawer(GravityCompat.START);
        });


        headerLayout = findViewById(R.id.header_root);

        int statusBarHeightId = getResources()
                .getIdentifier("status_bar_height", "dimen", "android");

        int statusBarHeight = statusBarHeightId > 0
                ? getResources().getDimensionPixelSize(statusBarHeightId)
                : 0;

        headerLayout.setPadding(0, statusBarHeight, 0, 0);


        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);


        headerLayout = findViewById(R.id.header_root);
        headerLeftIcon = findViewById(R.id.header_left_icon);
        headerMapIcon = findViewById(R.id.header_map_icon);
        headerTitle = findViewById(R.id.header_title);
        headerRightIcon = findViewById(R.id.header_right_icon);
        headerBottomBorder = findViewById(R.id.header_bottom_border);

        View bottomNav = findViewById(R.id.bottom_nav);


        locationManager = new AppLocationManager(this, this);
        locationManager.requestPermissionsAndStartUpdates();

        // 초기 HomeFragment 로드 로직 제거됨

        BottomNavigation.setup(bottomNav, tabId -> {
            if (tabId == R.id.tab_home) {
                headerLayout.setVisibility(View.VISIBLE);

                replaceFragment(new HomeFragment());

                setHeaderStyle(true);

            } else if (tabId == R.id.tab_hourly) {
                headerLayout.setVisibility(View.VISIBLE);

                replaceFragment(new HourlyFragment());

                setHeaderStyle(false);

            } else if (tabId == R.id.tab_daily) {
                headerLayout.setVisibility(View.VISIBLE);

                replaceFragment(new DailyFragment());

                setHeaderStyle(false);

            } else if (tabId == R.id.tab_calendar) {
                headerLayout.setVisibility(View.GONE);
                replaceFragment(new CalendarFragment());

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (locationManager != null && locationManager.checkPermissions()) {
            locationManager.startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.stopLocationUpdates();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationManager != null && locationManager.handlePermissionResult(requestCode, grantResults)) {
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onLocationReceived(double latitude, double longitude) {
        Log.d("MainActivity", "위치 수신 성공: Lat=" + latitude + ", Lon=" + longitude);

        sharedViewModel.setLocation(latitude, longitude);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment == null) {
            HomeFragment homeFragment = new HomeFragment();
            replaceFragment(homeFragment);
            setHeaderStyle(true);
        }

        locationManager.stopLocationUpdates();
    }

    @Override
    public void onPermissionDenied() {
        Log.w("MainActivity", "위치 권한 거부됨. 기본 위치 사용.");

        sharedViewModel.setLocation(37.5665, 126.9780);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment == null) {
            HomeFragment homeFragment = new HomeFragment();
            replaceFragment(homeFragment);
            setHeaderStyle(true);
        }
    }

    @Override
    public void onLocationFailed(String error) {
        Log.e("MainActivity", "위치 로드 실패: " + error);

        sharedViewModel.setLocation(37.5665, 126.9780);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment == null) {
            HomeFragment homeFragment = new HomeFragment();
            replaceFragment(homeFragment);
            setHeaderStyle(true);
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