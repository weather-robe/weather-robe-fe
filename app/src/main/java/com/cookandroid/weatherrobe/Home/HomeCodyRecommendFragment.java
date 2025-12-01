package com.cookandroid.weatherrobe.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.cookandroid.weatherrobe.Home.dto.HomeResDTO;
import com.cookandroid.weatherrobe.Home.model.SharedWeatherViewModel;
import com.cookandroid.weatherrobe.R;
import com.cookandroid.weatherrobe.RetrofitClient;
import com.cookandroid.weatherrobe.common.CommonApiResponse;
import com.facebook.shimmer.ShimmerFrameLayout; // ShimmerFrameLayout 임포트

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeCodyRecommendFragment extends Fragment {

    private static final String TAG = "CodyRecommendFragment";
    private SharedWeatherViewModel sharedViewModel;
    private LinearLayout llImageContainer;
    private ShimmerFrameLayout shimmerViewContainer; // ShimmerFrameLayout 참조 변수
    private View codyScrollView; // HorizontalScrollView 참조 변수

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_cody_recommend, container, false);

        llImageContainer = view.findViewById(R.id.ll_image_container);

        // ShimmerFrameLayout과 실제 스크롤뷰 참조
        View includeView = view.findViewById(R.id.simmer_layout);
        shimmerViewContainer = includeView.findViewById(R.id.shimmer_view_container);
        codyScrollView = view.findViewById(R.id.cody_scrollview);

        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedWeatherViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 초기 상태: Shimmer 시작 (API 호출 전)
        showSimmer();

        sharedViewModel.getWeatherId().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer weatherId) {
                if (weatherId != null && weatherId > 0) {
                    fetchWeatherImages(1, weatherId);
                }
            }
        });
    }

    private void fetchWeatherImages(int userId, int weatherId) {

        HomeApi apiService = RetrofitClient.getClient("https://api.weather-robe.kro.kr/")
                .create(HomeApi.class);

        Log.d(TAG, "이미지 요청: User ID " + userId + ", Weather ID " + weatherId);

        Call<CommonApiResponse<HomeResDTO.PostImageDTO>> call =
                apiService.getWeatherImages(userId, weatherId);

        call.enqueue(new Callback<CommonApiResponse<HomeResDTO.PostImageDTO>>() {
            @Override
            public void onResponse(
                    @NonNull Call<CommonApiResponse<HomeResDTO.PostImageDTO>> call,
                    @NonNull Response<CommonApiResponse<HomeResDTO.PostImageDTO>> res) {

                if (res.isSuccessful() && res.body() != null) {

                    HomeResDTO.PostImageDTO data = res.body().getSuccess();

                    if (data != null) {
                        Log.d(TAG, "이미지 로드 성공");
                        List<String> imageUrls = data.getImages();

                        updateImageUI(imageUrls);

                    } else {
                        String errorMsg = res.body().getError() != null ? res.body().getError().toString() : "데이터 없음";
                        Log.e(TAG, "응답 데이터 실패: " + errorMsg);
                    }
                } else {
                    Log.e(TAG, "API 호출 실패. Status: " + res.code() + ", Message: " + (res.errorBody() != null ? res.errorBody().toString() : "N/A"));
                }

                // 성공/실패와 관계없이 API 응답이 오면 Shimmer 중지
                hideSimmer();
            }

            @Override
            public void onFailure(
                    @NonNull Call<CommonApiResponse<HomeResDTO.PostImageDTO>> call,
                    @NonNull Throwable t) {
                Log.e(TAG, "API 통신 오류 발생", t);
                // 통신 실패 시에도 Shimmer 중지
                hideSimmer();
            }
        });
    }

    private void updateImageUI(List<String> imageUrls) {
        if (!isAdded() || llImageContainer == null) return;

        llImageContainer.removeAllViews();

        if (imageUrls != null && !imageUrls.isEmpty()) {

            for (int i = 0; i < imageUrls.size(); i++) {
                String imageUrl = imageUrls.get(i);

                ImageView imageView = new ImageView(requireContext());

                int widthPx = dpToPx(300);
                int heightPx = dpToPx(400);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPx, heightPx);

                // 마지막 이미지가 아니면 마진 추가 (12dp 사용)
                if (i < imageUrls.size() - 1) {
                    params.rightMargin = dpToPx(12);
                }

                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                Glide.with(this)
                        .load(imageUrl)
                        .into(imageView);

                llImageContainer.addView(imageView);
            }
            Log.d(TAG, "코디 이미지 " + imageUrls.size() + "개 업데이트 완료.");
        } else {
            Log.d(TAG, "받은 코디 이미지가 없습니다.");
        }
    }

    // Shimmer View를 표시하고 애니메이션 시작, 실제 View를 숨기는 메소드
    private void showSimmer() {
        if (shimmerViewContainer != null) {
            shimmerViewContainer.setVisibility(View.VISIBLE);
            shimmerViewContainer.startShimmer(); // Shimmer 애니메이션 시작
        }
        if (codyScrollView != null) {
            codyScrollView.setVisibility(View.GONE);
        }
    }

    // Shimmer View를 숨기고 애니메이션 중지, 실제 View를 표시하는 메소드
    private void hideSimmer() {
        if (shimmerViewContainer != null) {
            shimmerViewContainer.stopShimmer(); // Shimmer 애니메이션 중지
            shimmerViewContainer.setVisibility(View.GONE);
        }
        if (codyScrollView != null) {
            codyScrollView.setVisibility(View.VISIBLE);
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }
}