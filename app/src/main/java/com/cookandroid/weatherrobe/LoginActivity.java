package com.cookandroid.weatherrobe;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText inputId;
    private EditText inputPw;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);   // 네 로그인 XML 이름

        inputId = findViewById(R.id.input_id);
        inputPw = findViewById(R.id.input_pw);
        btnLogin = findViewById(R.id.btn_login);

        // 텍스트 변경 감지
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        inputId.addTextChangedListener(watcher);
        inputPw.addTextChangedListener(watcher);

        updateButton(); // 초기 상태 설정
    }

    private void updateButton() {
        String id = inputId.getText().toString().trim();
        String pw = inputPw.getText().toString().trim();

        if (!id.isEmpty() && !pw.isEmpty()) {
            // 둘 다 입력됨 → active PNG
            btnLogin.setBackgroundResource(R.drawable.login_active);
        } else {
            // 비어 있음 → default PNG
            btnLogin.setBackgroundResource(R.drawable.login_default);
        }
    }
}
