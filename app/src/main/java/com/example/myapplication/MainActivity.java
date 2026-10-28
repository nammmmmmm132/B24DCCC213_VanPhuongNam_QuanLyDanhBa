package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/** Màn hình 1: Đăng nhập. */
public class MainActivity extends AppCompatActivity {

    // Tài khoản cố định
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123456";

    private EditText etUsername, etPassword;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Ui.applyInsets(findViewById(R.id.main));

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.tvError);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> login());
        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login();
                return true;
            }
            return false;
        });
    }

    private void login() {
        String user = etUsername.getText().toString().trim();
        String pass = etPassword.getText().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            showError(getString(R.string.err_empty_login));
            return;
        }
        if (!USERNAME.equals(user) || !PASSWORD.equals(pass)) {
            showError(getString(R.string.err_wrong_login));
            return;
        }

        tvError.setVisibility(android.view.View.GONE);
        Intent intent = new Intent(this, ContactListActivity.class);
        intent.putExtra(ContactListActivity.EXTRA_USERNAME, user);
        startActivity(intent);
        finish(); // không cho Back quay lại màn hình đăng nhập
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(android.view.View.VISIBLE);
    }
}
