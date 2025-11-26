package com.cookandroid.weatherrobe;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

public class SignupActivity extends AppCompatActivity {

    private EditText inputId, inputPw, inputPwCheck, inputEmail;
    private CheckBox serviceCheck, privacyCheck;
    private TextView serviceView, privacyView;
    private ImageView btnCheck;

    private LinearLayout idErrorLayout, pwErrorLayout, pwCheckErrorLayout, emailErrorLayout;
    private ImageView idWarn, pwWarn, pwCheckWarn, emailWarn;
    private TextView idError, pwError, pwCheckError, emailError;

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

        btnCheck.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setValidationWatcher() {
        Log.d("WATCHER", "text watcher added");

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("WATCHER", "typing detected: " + s);
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
