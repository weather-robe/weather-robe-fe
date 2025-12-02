package com.cookandroid.weatherrobe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log; // Log import

import com.cookandroid.weatherrobe.Home.HomeFragment;
import com.cookandroid.weatherrobe.hourly.HourlyFragment;
import com.cookandroid.weatherrobe.daily.DailyFragment;
import com.cookandroid.weatherrobe.calendar.CalendarFragment;
import com.cookandroid.weatherrobe.location.AppLocationManager; // 추가
import com.cookandroid.weatherrobe.location.LocationUpdateListener; // 추가


public class MainActivity extends AppCompatActivity implements LocationUpdateListener {

    private View headerLayout;
    private ImageView headerLeftIcon;
    private ImageView headerMapIcon;
    private TextView headerTitle;
    private ImageView headerRightIcon;
    private View headerBottomBorder;

    private AppLocationManager locationManager;
    private double currentLatitude = 37.5665;
    private double currentLongitude = 126.9780;
    private boolean isLocationReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ImageView menuButton = findViewById(R.id.header_left_icon);

        menuButton.setOnClickListener(v -> {
            drawer.openDrawer(GravityCompat.START);
        });


        headerLayout = findViewById(R.id.header_root);

        // 상태바 높이 계산해서 헤더에 자동 적용
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

        // 초기 HomeFragment 로드
        replaceFragment(new HomeFragment());
        setHeaderStyle(true);

        BottomNavigation.setup(bottomNav, tabId -> {
            if (tabId == R.id.tab_home) {
                headerLayout.setVisibility(View.VISIBLE);

                HomeFragment homeFragment = new HomeFragment();
                replaceFragment(homeFragment);

                if (isLocationReady) {
                    homeFragment.updateLocation(currentLatitude, currentLongitude);
                }

                setHeaderStyle(true);

            } else if (tabId == R.id.tab_hourly) {
                headerLayout.setVisibility(View.VISIBLE);
                replaceFragment(new HourlyFragment());

                setHeaderStyle(false);

            } else if (tabId == R.id.tab_daily) {
                headerLayout.setVisibility(View.VISIBLE);

                DailyFragment dailyFragment = new DailyFragment();
                replaceFragment(dailyFragment);

                if (isLocationReady) {
                    dailyFragment.updateLocation(currentLatitude, currentLongitude);
                }

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
            // AppLocationManager에서 처리 완료
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onLocationReceived(double latitude, double longitude) {
        Log.d("MainActivity", "위치 수신 성공: Lat=" + latitude + ", Lon=" + longitude);
        this.currentLatitude = latitude;
        this.currentLongitude = longitude;
        this.isLocationReady = true;

        notifyCurrentFragment(latitude, longitude);

        locationManager.stopLocationUpdates();
    }

    @Override
    public void onPermissionDenied() {
        Log.w("MainActivity", "위치 권한 거부됨. 기본 위치 사용.");
        this.isLocationReady = true;
        notifyCurrentFragment(currentLatitude, currentLongitude);
    }

    @Override
    public void onLocationFailed(String error) {
        Log.e("MainActivity", "위치 로드 실패: " + error);
        this.isLocationReady = true;
        notifyCurrentFragment(currentLatitude, currentLongitude);
    }

    private void notifyCurrentFragment(double latitude, double longitude) {
        // 프래그먼트가 이미 화면에 있다면 findFragmentById로 찾습니다.
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment instanceof HomeFragment) {
            ((HomeFragment) currentFragment).updateLocation(latitude, longitude);
        }

        if (currentFragment instanceof DailyFragment) {
            ((DailyFragment) currentFragment).updateLocation(latitude, longitude);
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