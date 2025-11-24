package com.cookandroid.weatherrobe;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText inputId, inputPw, inputPwCheck, inputEmail;
    private CheckBox serviceCheck, privacyCheck;
    private TextView serviceView, privacyView;
    private ImageView btnCheck;

    // 에러 레이아웃
    private LinearLayout idErrorLayout, pwErrorLayout, pwCheckErrorLayout, emailErrorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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

        serviceCheck = findViewById(R.id.service_check);
        privacyCheck = findViewById(R.id.privacy_check);

        serviceView = findViewById(R.id.service_view);
        privacyView = findViewById(R.id.privacy_view);

        btnCheck = findViewById(R.id.btn_check);

        idErrorLayout = findViewById(R.id.id_error_layout);
        pwErrorLayout = findViewById(R.id.pw_error_layout);
        pwCheckErrorLayout = findViewById(R.id.pw_check_error_layout);
        emailErrorLayout = findViewById(R.id.email_error_layout);
    }

    private void setListeners() {

        // 약관 모달
        serviceView.setOnClickListener(v -> openDialog(R.layout.service_dialog));
        privacyView.setOnClickListener(v -> openDialog(R.layout.privacy_dialog));

        // 회원가입 완료 → 로그인 화면 이동
        btnCheck.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setValidationWatcher() {

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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

        serviceCheck.setOnCheckedChangeListener((buttonView, isChecked) -> updateCheckButtonState());
        privacyCheck.setOnCheckedChangeListener((buttonView, isChecked) -> updateCheckButtonState());
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

        idErrorLayout.setVisibility(idOk ? View.GONE : View.VISIBLE);
        pwErrorLayout.setVisibility(pwOk ? View.GONE : View.VISIBLE);
        pwCheckErrorLayout.setVisibility(pwMatchOk ? View.GONE : View.VISIBLE);
        emailErrorLayout.setVisibility(emailOk ? View.GONE : View.VISIBLE);
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
        if (isAllValid()) {
            btnCheck.setBackgroundResource(R.drawable.check_btn);
            btnCheck.setEnabled(true);
        } else {
            btnCheck.setBackgroundResource(R.drawable.check_default);
            btnCheck.setEnabled(false);
        }
    }

    private void openDialog(int layoutRes) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(layoutRes);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dpToPx(607)
        );

        ImageView closeBtn = dialog.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private int dpToPx(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale);
    }
}
