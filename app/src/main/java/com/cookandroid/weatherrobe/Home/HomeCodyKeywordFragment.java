package com.cookandroid.weatherrobe.Home;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cookandroid.weatherrobe.Home.dto.HomeResDTO;
import com.cookandroid.weatherrobe.Home.model.SharedWeatherViewModel;

import com.cookandroid.weatherrobe.R;
import com.cookandroid.weatherrobe.RetrofitClient;
import com.cookandroid.weatherrobe.common.CommonApiResponse;
import com.cookandroid.weatherrobe.common.CommonUtils;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeCodyKeywordFragment extends Fragment {

    private SharedWeatherViewModel sharedViewModel;

    private View dataLayoutWrapper;
    private ShimmerFrameLayout shimmerContainer;

    private TextView tvWeatherText;
    private LinearLayout llKeywordLayout;

    private int lastLoadedWeatherId = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_cody_keyword, container, false);

        View shimmerLayoutWrapper = view.findViewById(R.id.simmer_layout_wrapper);
        if (shimmerLayoutWrapper != null) {
            shimmerContainer = shimmerLayoutWrapper.findViewById(R.id.shimmer_container);
            if (shimmerContainer != null) {
                shimmerContainer.setAlpha(1.0f);
            }
        } else {
            Log.e("CodyFragment", "Simmer layout wrapper not found.");
        }

        dataLayoutWrapper = view.findViewById(R.id.data_layout_wrapper);

        if (dataLayoutWrapper instanceof LinearLayout) {
            tvWeatherText = dataLayoutWrapper.findViewById(R.id.tv_weather_description);
            llKeywordLayout = dataLayoutWrapper.findViewById(R.id.ll_keyword_tags);

            if (tvWeatherText == null || llKeywordLayout == null) {
                Log.e("CodyFragment", "Required data views (TV/LL tags) not found inside dataLayoutWrapper.");
            }
        } else {
            Log.e("CodyFragment", "Data layout wrapper not found or is not a LinearLayout.");
        }

        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedWeatherViewModel.class);

        startShimmerLoading();

        sharedViewModel.getWeatherId().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer weatherId) {
                if (weatherId != null && weatherId > 0 && weatherId != lastLoadedWeatherId) {
                    lastLoadedWeatherId = weatherId;
                    startShimmerLoading();
                    fetchWeatherKeywords(1, weatherId);
                }
            }
        });

        return view;
    }

    private void startShimmerLoading() {
        if (shimmerContainer != null && dataLayoutWrapper != null) {
            dataLayoutWrapper.setVisibility(View.GONE);

            shimmerContainer.setVisibility(View.GONE);

            if (shimmerContainer.getParent() instanceof View) {
                ((View) shimmerContainer.getParent()).setVisibility(View.GONE);
            }
            dataLayoutWrapper.setVisibility(View.VISIBLE);
        }
    }

    private void stopShimmerLoading() {
        if (shimmerContainer != null && dataLayoutWrapper != null) {
            shimmerContainer.stopShimmer();

            if (shimmerContainer.getParent() instanceof View) {
                ((View) shimmerContainer.getParent()).setVisibility(View.GONE);
            }
            dataLayoutWrapper.setVisibility(View.VISIBLE);
        }
    }

    private void fetchWeatherKeywords(int userId, int weatherId) {
        HomeApi api = RetrofitClient.getClient("https://api.weather-robe.kro.kr/")
                .create(HomeApi.class);

        Log.d("CodyAPI", "키워드 요청: User ID " + userId + ", Weather ID " + weatherId);

        api.getWeatherKeywords(userId, weatherId)
                .enqueue(new Callback<CommonApiResponse<HomeResDTO.PostKeywordDTO>>() {
                    @Override
                    public void onResponse(@NonNull Call<CommonApiResponse<HomeResDTO.PostKeywordDTO>> call,
                                           @NonNull Response<CommonApiResponse<HomeResDTO.PostKeywordDTO>> res) {

                        stopShimmerLoading();

                        if (res.isSuccessful() && res.body() != null && res.body().getSuccess() != null) {
                            Log.d("API_CALL", "키워드 로드 성공");
                            HomeResDTO.Weather weatherData = res.body().getSuccess().getWeather();
                            updateKeywordUI(weatherData);

                        } else {
                            Log.e("API_CALL", "응답 실패. Status: " + res.code() + ", Message: " + (res.body() != null ? res.body().getError() : "N/A"));
                            updateKeywordUI(null);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CommonApiResponse<HomeResDTO.PostKeywordDTO>> call, @NonNull Throwable t) {
                        Log.e("CodyAPI", "키워드 요청 실패: " + t.getMessage());
                        stopShimmerLoading();
                        updateKeywordUI(null);
                    }
                });
    }

    private void updateKeywordUI(HomeResDTO.Weather weatherData) {
        if (!isAdded()) return;

        if (weatherData == null) {
            if (tvWeatherText != null) {
                tvWeatherText.setText("날씨 정보를 불러오는 데 실패했습니다.");
            }
            if (llKeywordLayout != null) {
                llKeywordLayout.removeAllViews();
            }
            lastLoadedWeatherId = 0;
            return;
        }

        String description = weatherData.getText();
        if (tvWeatherText != null) {
            if (description != null) {
                tvWeatherText.setText(description);
            } else {
                tvWeatherText.setText("오늘의 날씨 정보가 없습니다.");
            }
        }

        List<String> keywords = weatherData.getKeywords();
        if (llKeywordLayout != null) {
            llKeywordLayout.removeAllViews();

            if (keywords != null && !keywords.isEmpty()) {
                for (String keyword : keywords) {
                    TextView tagView = createKeywordTag(keyword);
                    llKeywordLayout.addView(tagView);
                }
                Log.d("CodyUI", "코디 키워드 " + keywords.size() + "개 업데이트 완료.");
            } else {
                Log.d("CodyUI", "받은 코디 키워드가 없습니다.");
            }
        }
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    private TextView createKeywordTag(String keyword) {
        Context context = getContext();
        if (context == null) return new TextView(requireContext());

        TextView textView = new TextView(context);
        textView.setText(CommonUtils.addKeywordIcon(keyword));

        textView.setBackgroundResource(R.drawable.label_bg);

        textView.setTextColor(ContextCompat.getColor(context, R.color.text_dark));

        try {
            textView.setTypeface(ResourcesCompat.getFont(context, R.font.nanumbarungothic));
        } catch (Exception e) {
            Log.e("FontLoad", "폰트 nanumbarungothic 로드 실패", e);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(0, 0, dpToPx(6), 0);
        textView.setLayoutParams(params);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);

        return textView;
    }
}