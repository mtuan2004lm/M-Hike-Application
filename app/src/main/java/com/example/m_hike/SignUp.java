package com.example.m_hike;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    EditText nameInput, emailInput, phoneInput, passwordInput, confirmPasswordInput;
    Button signupButton;
    InformationDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        db = new InformationDatabase(this);

        // ÁNH XẠ UI (Dựa trên ID phổ biến trong layout Đăng ký của bạn)
        nameInput = findViewById(R.id.editTextText);
        emailInput = findViewById(R.id.editTextText2);
        phoneInput = findViewById(R.id.editTextText3);
        passwordInput = findViewById(R.id.editTextTextPassword2);
        confirmPasswordInput = findViewById(R.id.editTextTextPassword3);
        signupButton = findViewById(R.id.submit_button); // Hoặc ID nút Đăng ký của bạn

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });
    }

    private void attemptSignup() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ tất cả các trường.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp.", Toast.LENGTH_LONG).show();
            return;
        }

        long result = db.insertUser(name, email, phone, password);

        if (result > 0) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_LONG).show();
            // Tự động chuyển sang màn hình Đăng nhập
            Intent intent = new Intent(SignUp.this, SignIn.class);
            startActivity(intent);
            finish();
        } else if (result == -1) {
            Toast.makeText(this, "Email này đã được đăng ký. Vui lòng sử dụng email khác.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_LONG).show();
        }
    }
}