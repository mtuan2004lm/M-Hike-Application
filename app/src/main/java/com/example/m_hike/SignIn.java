package com.example.m_hike;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginButton, backButton;
    InformationDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        db = new InformationDatabase(this);

        // ÁNH XẠ UI (Dựa trên ID đã được thống nhất: et_email, et_password, v.v.)
        emailInput = findViewById(R.id.et_email);
        passwordInput = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.btn_login);
        backButton = findViewById(R.id.btn_back_welcome);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Quay lại màn hình WelcomePage
                Intent intent = new Intent(SignIn.this, WelcomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void attemptLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email và mật khẩu.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean loginSuccess = db.checkUser(email, password);

        if (loginSuccess) {
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_LONG).show();

            // CHUYỂN HƯỚNG SANG HOMEPAGE
            Intent intent = new Intent(SignIn.this, HomePage.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "Email hoặc mật khẩu không đúng.", Toast.LENGTH_LONG).show();
        }
    }
}