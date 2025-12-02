package com.cookandroid.weatherrobe.login;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.weatherrobe.MainActivity;
import com.cookandroid.weatherrobe.R;
import com.cookandroid.weatherrobe.signup.SignupActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText inputId;
    private EditText inputPw;
    private ImageView btnLogin;
    private TextView textSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputId = findViewById(R.id.input_id);
        inputPw = findViewById(R.id.input_pw);
        btnLogin = findViewById(R.id.btn_login);
        textSignup = findViewById(R.id.text_signup);

        textSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButton();
            }
        };

        inputId.addTextChangedListener(watcher);
        inputPw.addTextChangedListener(watcher);

        updateButton();

        btnLogin.setOnClickListener(v -> sendLoginRequest());
    }

    private void updateButton() {
        String id = inputId.getText().toString().trim();
        String pw = inputPw.getText().toString().trim();

        if (!id.isEmpty() && !pw.isEmpty()) {
            btnLogin.setBackgroundResource(R.drawable.login_active);
        } else {
            btnLogin.setBackgroundResource(R.drawable.login_default);
        }
    }

        private void sendLoginRequest() {
            String id = inputId.getText().toString().trim();
            String pw = inputPw.getText().toString().trim();

            LoginRequest body = new LoginRequest(id, pw);

            Call<LoginResponse> call = RetrofitClient.getLoginService().login(body);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    if (!response.isSuccessful()) {
                        showLoginFailDialog();
                        return;
                    }

                    LoginResponse res = response.body();
                    if (res == null || !"SUCCESS".equals(res.resultType)) {
                        showLoginFailDialog();
                        return;
                    }

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    showLoginFailDialog();
                }
            });
        }

    private void showLoginFailDialog() {
        View view = getLayoutInflater().inflate(R.layout.login_error, null);
        ImageView confirmBtn = view.findViewById(R.id.btn_confirm);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(view);
        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setDimAmount(0.6f);

            window.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
            window.setGravity(Gravity.CENTER);
        }

        confirmBtn.setOnClickListener(v -> dialog.dismiss());
    }
}
