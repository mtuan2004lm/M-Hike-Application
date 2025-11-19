package com.example.m_hike;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton; // <<< ĐÃ THÊM: KHẮC PHỤC LỖI
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class AddHikeActivity extends AppCompatActivity {

    private TextInputEditText editTextHikeName, editTextLocation, editTextDate, editTextLength,
            editTextDescription, editTextCustomWeather, editTextCustomEquipment;
    private Spinner spinnerDifficulty;
    private RadioGroup radioGroupParking;
    private Button buttonSaveHike;

    private HikeDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Đảm bảo tên layout khớp với file XML của bạn
        setContentView(R.layout.activity_add_hike);

        mapViews();
        dbHelper = new HikeDatabaseHelper(this);
        setupDifficultySpinner();

        // Xử lý sự kiện DatePicker
        editTextDate.setOnClickListener(v -> showDatePicker());

        // Xử lý sự kiện Save
        buttonSaveHike.setOnClickListener(v -> validateAndSaveHike());
    }

    private void mapViews() {
        // Ánh xạ các trường bắt buộc
        editTextHikeName = findViewById(R.id.editTextHikeName);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextDate = findViewById(R.id.editTextDate);
        editTextLength = findViewById(R.id.editTextLength);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);
        radioGroupParking = findViewById(R.id.radioGroupParking);

        // Ánh xạ các trường tùy chọn/sáng tạo
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextCustomWeather = findViewById(R.id.editTextCustomWeather);
        editTextCustomEquipment = findViewById(R.id.editTextCustomEquipment);

        buttonSaveHike = findViewById(R.id.buttonSaveHike);
    }

    private void setupDifficultySpinner() {
        // Cần định nghĩa mảng `difficulty_levels` trong res/values/arrays.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.difficulty_levels,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year1);
                    editTextDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void validateAndSaveHike() {
        String name = editTextHikeName.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String lengthStr = editTextLength.getText().toString().trim();
        String difficulty = spinnerDifficulty.getSelectedItem().toString();

        // --- Logic lấy giá trị RadioButton ---
        int selectedRadioButtonId = radioGroupParking.getCheckedRadioButtonId();
        // Lỗi "Cannot resolve symbol 'RadioButton'" đã được khắc phục bằng cách import
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String parking = (selectedRadioButton != null && selectedRadioButtonId != -1) ? selectedRadioButton.getText().toString() : "";

        // --- Xác thực các trường bắt buộc ---
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(location) || TextUtils.isEmpty(date) || TextUtils.isEmpty(lengthStr) || TextUtils.isEmpty(parking)) {
            Toast.makeText(this, "Please fill in all required fields (*)", Toast.LENGTH_LONG).show();
            return;
        }

        double length;
        try {
            length = Double.parseDouble(lengthStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Length must be a valid number.", Toast.LENGTH_LONG).show();
            return;
        }

        // Thu thập các trường tùy chọn/sáng tạo
        String description = editTextDescription.getText().toString().trim();
        String customWeather = editTextCustomWeather.getText().toString().trim();
        String customEquipment = editTextCustomEquipment.getText().toString().trim();

        // Hiển thị cửa sổ xác nhận
        showConfirmationDialog(name, location, date, length, difficulty, parking, description, customWeather, customEquipment);
    }

    private void showConfirmationDialog(String name, String location, String date, double length, String difficulty,
                                        String parking, String description, String customWeather, String customEquipment) {

        String confirmationMessage = "Are you sure you want to save this hike?\n\n" +
                "Name: " + name + "\n" +
                "Location: " + location + "\n" +
                "Date: " + date + "\n" +
                "Length: " + length + " km\n" +
                "Difficulty: " + difficulty + "\n" +
                "Parking: " + parking + "\n" +
                "Weather: " + customWeather + "\n" +
                "Equipment: " + customEquipment;

        new AlertDialog.Builder(this)
                .setTitle("Confirm Hike Details")
                .setMessage(confirmationMessage)
                .setPositiveButton("SAVE", (dialog, which) -> {
                    performSave(name, location, date, length, difficulty, parking, description, customWeather, customEquipment);
                })
                .setNegativeButton("EDIT", null)
                .show();
    }

    private void performSave(String name, String location, String date, double length, String difficulty,
                             String parking, String description, String customWeather, String customEquipment) {

        Hike hike = new Hike();
        hike.setName(name);
        hike.setLocation(location);
        hike.setDate(date);
        hike.setParkingAvailable(parking);
        hike.setLength(length);
        hike.setDifficultyLevel(difficulty);
        hike.setDescription(description);
        hike.setWeatherCondition(customWeather);
        hike.setEquipmentRequired(customEquipment);

        long result = dbHelper.addHike(hike);

        if (result > 0) {
            Toast.makeText(this, "Hike '" + name + "' saved successfully!", Toast.LENGTH_LONG).show();
            finish(); // Đóng Activity và quay lại HomePage
        } else {
            Toast.makeText(this, "Error saving hike.", Toast.LENGTH_LONG).show();
        }
    }
}