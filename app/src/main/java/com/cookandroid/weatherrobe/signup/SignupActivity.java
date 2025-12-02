package com.cookandroid.weatherrobe.signup;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.weatherrobe.login.LoginActivity;
import com.cookandroid.weatherrobe.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText inputId, inputPw, inputPwCheck, inputEmail;
    private CheckBox serviceCheck, privacyCheck;
    private TextView serviceView, privacyView;
    private FrameLayout btnCheck;

    private LinearLayout idErrorLayout, pwErrorLayout, pwCheckErrorLayout, emailErrorLayout;
    private ImageView idWarn, pwWarn, pwCheckWarn, emailWarn;
    private TextView idError, pwError, pwCheckError, emailError;
    private ImageView pwToggle, pwCheckToggle;
    private boolean isPwVisible = false;
    private boolean isPwCheckVisible = false;

    private signupService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        );

        api = RetrofitClient.getInstance().create(signupService.class);

        initViews();
        setListeners();
        setValidationWatcher();
        updateCheckButtonState();

    }

    private void initViews() {
        inputId = findViewById(R.id.input_id);
        inputPw = findViewById(R.id.input_pw);
        inputPwCheck = findViewById(R.id.input_pw_check);
        inputEmail = findViewById(R.id.input_email);

        pwToggle = findViewById(R.id.pw_toggle);
        pwCheckToggle = findViewById(R.id.pw_check_toggle);

        serviceCheck = findViewById(R.id.service_check);
        privacyCheck = findViewById(R.id.privacy_check);

        serviceView = findViewById(R.id.service_view);
        privacyView = findViewById(R.id.privacy_view);

        btnCheck = findViewById(R.id.btn_check);

        idErrorLayout = findViewById(R.id.id_error_layout);
        idWarn = findViewById(R.id.id_warn);
        idError = findViewById(R.id.id_error);
        pwErrorLayout = findViewById(R.id.pw_error_layout);
        pwWarn = findViewById(R.id.pw_warn);
        pwError = findViewById(R.id.pw_error);
        pwCheckErrorLayout = findViewById(R.id.pw_check_error_layout);
        pwCheckWarn = findViewById(R.id.pw_check_warn);
        pwCheckError = findViewById(R.id.pw_check_error);
        emailErrorLayout = findViewById(R.id.email_error_layout);
        emailWarn = findViewById(R.id.email_warn);
        emailError = findViewById(R.id.email_error);
    }

    private void setListeners() {

        serviceView.setOnClickListener(v -> openDialog(R.layout.service_dialog));
        privacyView.setOnClickListener(v -> openDialog(R.layout.privacy_dialog));

        btnCheck.setOnTouchListener((v, event) -> {
            v.performClick();
            return false;
        });

        btnCheck.setOnClickListener(v -> {
            Log.d("BTN", "clicked!!");
            sendSignupRequest();
        });

        pwToggle.setOnClickListener(v -> {
            togglePassword(inputPw, pwToggle, isPwVisible);
            isPwVisible = !isPwVisible;
        });

        pwCheckToggle.setOnClickListener(v -> {
            togglePassword(inputPwCheck, pwCheckToggle, isPwCheckVisible);
            isPwCheckVisible = !isPwCheckVisible;
        });
    }

    private void togglePassword(EditText editText, ImageView toggleIcon, boolean isVisibleFlag) {
        if (isVisibleFlag) {
            editText.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
            toggleIcon.setImageResource(R.drawable.ic_eye_off);
        } else {
            editText.setTransformationMethod(null);
            toggleIcon.setImageResource(R.drawable.ic_eye_on);
        }

        editText.setSelection(editText.getText().length());
    }

    private void setValidationWatcher() {
        Log.d("WATCHER", "text watcher added");

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("WATCHER", "typing detected: " + s);

                idErrorLayout.setVisibility(View.GONE);
                emailErrorLayout.setVisibility(View.GONE);

                validateFields();
                updateCheckButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        inputId.addTextChangedListener(watcher);
        inputPw.addTextChangedListener(watcher);
        inputPwCheck.addTextChangedListener(watcher);
        inputEmail.addTextChangedListener(watcher);

        serviceCheck.setOnCheckedChangeListener((buttonView, isChecked) ->{
            Log.d("CHECK", "service = " + isChecked);
            updateCheckButtonState();
        });
        privacyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d("CHECK", "privacy = " + isChecked);
            updateCheckButtonState();
        });
    }

    private void validateFields() {
        String id = inputId.getText().toString().trim();
        String pw = inputPw.getText().toString().trim();
        String pwCheck = inputPwCheck.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();

        boolean idOk = id.matches("^[a-z0-9]{6,20}$");
        boolean pwOk = pw.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,20}$");
        boolean pwMatchOk = pw.equals(pwCheck) && pwCheck.length() > 0;
        boolean emailOk = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

        // --- ID ---
        if (id.length() == 0) {
            idErrorLayout.setVisibility(View.GONE);
        } else {
            if (idOk) {
                idErrorLayout.setVisibility(View.GONE);
            } else {
                idErrorLayout.setVisibility(View.VISIBLE);
            }
        }
        idWarn.setVisibility(idErrorLayout.getVisibility());
        idError.setVisibility(idErrorLayout.getVisibility());

        // --- PW ---
        if (pw.length() == 0) {
            pwErrorLayout.setVisibility(View.GONE);
        } else {
            pwErrorLayout.setVisibility(pwOk ? View.GONE : View.VISIBLE);
        }
        pwWarn.setVisibility(pwErrorLayout.getVisibility());
        pwError.setVisibility(pwErrorLayout.getVisibility());

        // --- PW CHECK ---
        if (pwCheck.length() == 0) {
            pwCheckErrorLayout.setVisibility(View.GONE);
        } else {
            pwCheckErrorLayout.setVisibility(pwMatchOk ? View.GONE : View.VISIBLE);
        }
        pwCheckWarn.setVisibility(pwCheckErrorLayout.getVisibility());
        pwCheckError.setVisibility(pwCheckErrorLayout.getVisibility());

        // --- EMAIL ---
        if (email.length() == 0) {
            emailErrorLayout.setVisibility(View.GONE);
        } else {
            emailErrorLayout.setVisibility(emailOk ? View.GONE : View.VISIBLE);
        }
        emailWarn.setVisibility(emailErrorLayout.getVisibility());
        emailError.setVisibility(emailErrorLayout.getVisibility());
    }

    private boolean isAllInputValid() {
        String id = inputId.getText().toString().trim();
        String pw = inputPw.getText().toString().trim();
        String pwCheck = inputPwCheck.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();

        boolean idOk = id.matches("^[a-z0-9]{6,20}$");
        boolean pwOk = pw.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,20}$");
        boolean pwMatchOk = pw.equals(pwCheck) && pwCheck.length() > 0;
        boolean emailOk = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

        return idOk && pwOk && pwMatchOk && emailOk;
    }

    private boolean isAllValid() {
        return isAllInputValid() && serviceCheck.isChecked() && privacyCheck.isChecked();
    }

    private void updateCheckButtonState() {
        Log.d("BTN", "valid = " + isAllValid());
        if (isAllValid()) {
            btnCheck.setBackgroundResource(R.drawable.check_btn);
            btnCheck.setEnabled(true);
        } else {
            btnCheck.setBackgroundResource(R.drawable.check_default);
            btnCheck.setEnabled(false);
        }
    }

    private void sendSignupRequest() {

        Log.d("DEBUG", "sendSignupRequest() called");
        Log.d("DEBUG", "isAllValid = " + isAllValid());

        if (!isAllValid()){
            Log.d("DEBUG", "isAllValid = false → return됨");
            return;
        }

        Log.d("DEBUG", "API 요청 시작");

        String id = inputId.getText().toString().trim();
        String pw = inputPw.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();

        Map<String, String> body = new HashMap<>();
        body.put("loginId", id);
        body.put("password", pw);
        body.put("email", email);
        body.put("name", "이름"); // 필요하면 수정

        api.signup(body).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> res) {

                Log.d("DEBUG", "응답 도착");
                Log.d("DEBUG", "code = " + res.code());

                if (res.isSuccessful()) {
                    Log.d("SIGNUP", "회원가입 성공");
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();
                }
                else if (res.code() == 409) {
                    try {
                        String errorBody = res.errorBody().string();
                        JSONObject json = new JSONObject(errorBody);
                        JSONObject error = json.getJSONObject("error");
                        String errorCode = error.getString("errorCode");

                        Log.d("DEBUG", "409 errorBody = " + errorBody);
                        Log.d("DEBUG", "errorCode = " + errorCode);

                        // 입력 시 다시 사라지도록 기본은 숨김
                        idErrorLayout.setVisibility(View.GONE);
                        emailErrorLayout.setVisibility(View.GONE);

                        if (errorCode.equals("duplicate_loginId")) {
                            idErrorLayout.setVisibility(View.VISIBLE);
                            idWarn.setVisibility(View.VISIBLE);
                            idError.setVisibility(View.VISIBLE);
                            idError.setText("이미 존재하는 아이디입니다.");
                        }

                        if (errorCode.equals("duplicate_email")) {
                            emailErrorLayout.setVisibility(View.VISIBLE);
                            emailWarn.setVisibility(View.VISIBLE);
                            emailError.setVisibility(View.VISIBLE);
                            emailError.setText("이미 존재하는 이메일입니다.");
                        }

                    } catch (Exception e) {
                        Log.e("SIGNUP", "409 처리 중 JSON 파싱 오류");
                    }
                }
                else if (res.code() == 400) {
                    Log.e("SIGNUP", "잘못된 요청 (400)");
                }
                else {
                    Log.e("SIGNUP", "알 수 없는 오류: " + res.code());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("SIGNUP", "서버 연결 실패: " + t.getMessage());
            }
        });
    }


    private void openDialog(int layoutRes) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(layoutRes);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        ImageView closeBtn = dialog.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(v -> dialog.dismiss());
    }

    private int dpToPx(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale);
    }

}
