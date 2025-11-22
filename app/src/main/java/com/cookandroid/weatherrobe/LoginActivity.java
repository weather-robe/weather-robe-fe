package com.cookandroid.weatherrobe;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText inputId;
    private EditText inputPw;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputId = findViewById(R.id.input_id);
        inputPw = findViewById(R.id.input_pw);
        btnLogin = findViewById(R.id.btn_login);

        // 텍스트 변경 감지 → 버튼 상태 업데이트
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton();
            }
        };

        inputId.addTextChangedListener(watcher);
        inputPw.addTextChangedListener(watcher);

        updateButton(); // 초기 상태

        // 로그인 버튼 클릭 → 모달 호출
        btnLogin.setOnClickListener(v -> {
            String id = inputId.getText().toString().trim();
            String pw = inputPw.getText().toString().trim();

            // 임시 로그인 검증
            if (!id.equals("test") || !pw.equals("1234")) {
                showLoginFailDialog();
            }
        });
    }

    // 버튼 PNG 상태 변경
    private void updateButton() {
        String id = inputId.getText().toString().trim();
        String pw = inputPw.getText().toString().trim();

        if (!id.isEmpty() && !pw.isEmpty()) {
            btnLogin.setBackgroundResource(R.drawable.login_active);
        } else {
            btnLogin.setBackgroundResource(R.drawable.login_default);
        }
    }

    // 로그인 실패 모달
    private void showLoginFailDialog() {
        View view = getLayoutInflater().inflate(R.layout.login_error, null);
        Button confirmBtn = view.findViewById(R.id.btn_confirm);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            window.setDimAmount(0.6f);
        }

        // 확인 버튼 → 닫기
        confirmBtn.setOnClickListener(v -> dialog.dismiss());
    }
}
