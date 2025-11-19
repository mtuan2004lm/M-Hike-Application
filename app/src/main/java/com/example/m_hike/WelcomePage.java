package com.example.m_hike; // Đảm bảo package này đúng với package của bạn

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// THÊM HAI DÒNG IMPORT NÀY ĐỂ KHẮC PHỤC LỖI
import com.example.m_hike.SignUp;
import com.example.m_hike.SignIn;

public class WelcomePage extends AppCompatActivity {

    private Button signUpButton;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomepage);

        // 1. Ánh xạ các Button từ Layout XML
        signUpButton = findViewById(R.id.button);
        signInButton = findViewById(R.id.button2);

        // 2. Thiết lập OnClickListener cho nút SIGN UP
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang Signup Activity
                Intent intent = new Intent(WelcomePage.this, SignUp.class);
                startActivity(intent);
            }
        });

        // 3. Thiết lập OnClickListener cho nút SIGN IN
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang Signin Activity
                Intent intent = new Intent(WelcomePage.this, SignIn.class);
                startActivity(intent);
            }
        });
    }
}