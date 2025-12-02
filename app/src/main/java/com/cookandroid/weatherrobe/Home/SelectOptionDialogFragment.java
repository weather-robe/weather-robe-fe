package com.cookandroid.weatherrobe.Home;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cookandroid.weatherrobe.R;

import java.util.ArrayList;
import java.util.List;

public class SelectOptionDialogFragment extends DialogFragment {

    private final List<View> selectedCards = new ArrayList<>();

    public interface OnOptionSelectedListener {
        void onSelected(List<String> selectedKeys);
    }

    private OnOptionSelectedListener listener;

    public SelectOptionDialogFragment(OnOptionSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.home_weather_dialog, container, false);

        initCardClicks(root);
        applyDefaultSelection(root);
        initConfirmButton(root);

        return root;
    }

    /** 기본 선택 */
    private void applyDefaultSelection(View root) {
        View feel = root.findViewById(R.id.feel_card2);
        View pop = root.findViewById(R.id.pop_card2);
        View pm10 = root.findViewById(R.id.pm10_card2);

        selectCard(feel);
        selectCard(pop);
        selectCard(pm10);

        updateCurrentSelectionUI(root);
    }

    private void initCardClicks(View root) {
        View feel = root.findViewById(R.id.feel_card2);
        View pop = root.findViewById(R.id.pop_card2);
        View rain = root.findViewById(R.id.rain_card);
        View humidity = root.findViewById(R.id.humidity_card);
        View wind = root.findViewById(R.id.wind_card);
        View pm10 = root.findViewById(R.id.pm10_card2);
        View pm25 = root.findViewById(R.id.pm25_card);

        setToggle(feel, "FEEL");
        setToggle(pop, "POP");
        setToggle(rain, "RAIN");
        setToggle(humidity, "HUMIDITY");
        setToggle(wind, "WIND");
        setToggle(pm10, "PM10");
        setToggle(pm25, "PM25");
    }

    private void setToggle(View cardRoot, String key) {
        cardRoot.setTag(key);
        cardRoot.setOnClickListener(v -> toggleSelection(cardRoot));
    }

    /** 선택 강제 적용 */
    private void selectCard(View cardRoot) {
        if (!selectedCards.contains(cardRoot)) {
            cardRoot.setSelected(true);
            cardRoot.setBackgroundResource(R.drawable.home_dialog_card_selected);
            selectedCards.add(cardRoot);
        }
    }

    private void toggleSelection(View card) {
        boolean isSelected = card.isSelected();

        if (!isSelected && selectedCards.size() >= 3) return;

        if (isSelected) {
            card.setSelected(false);
            card.setBackgroundResource(R.drawable.home_dialog_card);
            selectedCards.remove(card);
        } else {
            card.setSelected(true);
            card.setBackgroundResource(R.drawable.home_dialog_card_selected);
            selectedCards.add(card);
        }

        updateCurrentSelectionUI(getView());
    }

    /** 선택된 카드 3개 미리보기 */
    private void updateCurrentSelectionUI(View root) {
        if (root == null) return;

        View[] slots = {
                root.findViewById(R.id.feel_card1),
                root.findViewById(R.id.pop_card1),
                root.findViewById(R.id.pm10_card1)
        };

        for (View s : slots) s.setVisibility(View.INVISIBLE);

        for (int i = 0; i < selectedCards.size() && i < 3; i++) {
            View src = selectedCards.get(i);
            View target = slots[i];

            target.setVisibility(View.VISIBLE);
            copyCardContent(src, target);
        }
    }

    /** 아이콘/텍스트 복사 */
    private void copyCardContent(View from, View to) {
        ImageView fromIcon = from.findViewById(R.id.card_icon);
        TextView fromValue = from.findViewById(R.id.card_value);
        TextView fromTitle = from.findViewById(R.id.card_title);

        ImageView toIcon = to.findViewById(R.id.card_icon);
        TextView toValue = to.findViewById(R.id.card_value);
        TextView toTitle = to.findViewById(R.id.card_title);

        toIcon.setImageDrawable(fromIcon.getDrawable());
        toValue.setText(fromValue.getText());
        toTitle.setText(fromTitle.getText());
    }

    private void initConfirmButton(View root) {
        root.findViewById(R.id.btn_check).setOnClickListener(v -> {
            List<String> result = new ArrayList<>();

            for (View card : selectedCards) {
                String tag = (String) card.getTag();
                if (tag != null) result.add(tag);
            }

            listener.onSelected(result);
            dismiss();
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            Window w = dialog.getWindow();
            if (w != null) {
                w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                w.setGravity(Gravity.TOP);

                WindowManager.LayoutParams params = w.getAttributes();
                params.y = dpToPx(87);
                w.setAttributes(params);

                w.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}
