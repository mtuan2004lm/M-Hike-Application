package com.example.m_hike;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup; // Đã sửa: Dùng RadioGroup
import android.widget.Spinner;     // Đã sửa: Dùng Spinner
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

// SỬ DỤNG LỚP NÀY CHO TRƯỜNG NHẬP LIỆU
import com.google.android.material.textfield.TextInputEditText;


public class HikeDetailActivity extends AppCompatActivity {

    // Khai báo Views
    private TextInputEditText editTextName, editTextLocation, editTextDate, editTextLength,
            editTextDescription, editTextWeather, editTextEquipment;

    // 🚨 KHẮC PHỤC LỖI BIÊN DỊCH: Đã sửa kiểu dữ liệu và ID 🚨
    private RadioGroup radioGroupParking;
    private Spinner spinnerDifficulty;

    private Button buttonEdit, buttonDelete, buttonObservations;

    // Khai báo Database Helper và Model
    private HikeDatabaseHelper dbHelper;
    private Hike currentHike;
    private int hikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);

        dbHelper = new HikeDatabaseHelper(this);

        // 1. Ánh xạ Views (Đảm bảo ID khớp với activity_hike_detail.xml)
        editTextName = findViewById(R.id.editTextHikeName);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextDate = findViewById(R.id.editTextDate);
        editTextLength = findViewById(R.id.editTextLength);

        // 🚨 SỬA LỖI COMPILATION: Ánh xạ đúng ID và Kiểu lớp 🚨
        radioGroupParking = findViewById(R.id.radioGroupParking);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);

        editTextDescription = findViewById(R.id.editTextDescription);
        editTextWeather = findViewById(R.id.editTextCustomWeather);
        editTextEquipment = findViewById(R.id.editTextCustomEquipment);

        buttonEdit = findViewById(R.id.buttonEdit);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonObservations = findViewById(R.id.buttonObservations);

        // 2. Lấy Hike ID từ Intent
        hikeId = getIntent().getIntExtra("HIKE_ID", -1);

        if (hikeId != -1) {
            loadHikeData(hikeId);
        } else {
            Toast.makeText(this, "Error: Hike ID not found.", Toast.LENGTH_LONG).show();
            finish();
        }

        // 3. Thiết lập Listeners
        buttonEdit.setOnClickListener(v -> goToEditHike());
        buttonDelete.setOnClickListener(v -> confirmDeleteHike());
        // Logic mới cho Observations
        buttonObservations.setOnClickListener(v -> goToObservations());
    }

    // Phương thức này có thể được gọi lại khi quay lại từ AddHikeActivity
    @Override
    protected void onResume() {
        super.onResume();
        if (hikeId != -1) {
            loadHikeData(hikeId);
        }
    }

    private void loadHikeData(int id) {
        currentHike = dbHelper.getHike(id);

        if (currentHike != null) {
            setTitle(currentHike.getName());

            // Đổ dữ liệu vào Views
            editTextName.setText(currentHike.getName());
            editTextLocation.setText(currentHike.getLocation());
            editTextDate.setText(currentHike.getDate());
            editTextLength.setText(String.valueOf(currentHike.getLength()));
            editTextDescription.setText(currentHike.getDescription());
            editTextWeather.setText(currentHike.getWeatherCondition());
            editTextEquipment.setText(currentHike.getEquipmentRequired());

            // Xử lý các trường đặc biệt (Spinner và RadioGroup)
            setDifficulty(currentHike.getDifficultyLevel());
            setParking(currentHike.getParkingAvailable());

            // Đặt các trường về chế độ chỉ đọc (read-only)
            setFieldsReadOnly(true);
        } else {
            Toast.makeText(this, "Hike not found.", Toast.LENGTH_LONG).show();
        }
    }

    // Hàm hỗ trợ đặt giá trị cho Difficulty Spinner
    private void setDifficulty(String difficulty) {
        // TODO: Bạn cần có Adapter cho Spinner để phương thức này hoạt động.
        // Ví dụ tạm thời (bỏ qua nếu bạn đã làm trong Adapter)
        // ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerDifficulty.getAdapter();
        // if (adapter != null) {
        //    spinnerDifficulty.setSelection(adapter.getPosition(difficulty));
        // }
    }

    // Hàm hỗ trợ đặt giá trị cho Parking RadioGroup
    private void setParking(String parking) {
        if (parking != null) {
            if (parking.equalsIgnoreCase("Yes")) {
                radioGroupParking.check(R.id.radioParkingYes);
            } else if (parking.equalsIgnoreCase("No")) {
                radioGroupParking.check(R.id.radioParkingNo);
            }
        }
    }

    private void setFieldsReadOnly(boolean readOnly) {
        // Đặt tất cả các trường về chế độ chỉ đọc
        editTextName.setEnabled(!readOnly);
        editTextLocation.setEnabled(!readOnly);
        editTextDate.setEnabled(!readOnly);
        editTextLength.setEnabled(!readOnly);
        editTextDescription.setEnabled(!readOnly);
        editTextWeather.setEnabled(!readOnly);
        editTextEquipment.setEnabled(!readOnly);

        // 🚨 XỬ LÝ CHO SPINNER VÀ RADIOGROUP 🚨
        spinnerDifficulty.setEnabled(!readOnly);

        // Vô hiệu hóa tất cả RadioButton trong RadioGroup
        for (int i = 0; i < radioGroupParking.getChildCount(); i++) {
            radioGroupParking.getChildAt(i).setEnabled(!readOnly);
        }
    }

    private void confirmDeleteHike() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Hike")
                .setMessage("Are you sure you want to delete this hike and all its observations?")
                .setPositiveButton("Yes", (dialog, which) -> deleteHike())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteHike() {
        // Hàm deleteHike trong DatabaseHelper phải đảm bảo xóa cả Observations liên quan
        int result = dbHelper.deleteHike(hikeId);
        if (result > 0) {
            Toast.makeText(this, "Hike deleted successfully!", Toast.LENGTH_SHORT).show();
            // Quay về HomePage và làm mới danh sách
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Error deleting Hike.", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToEditHike() {
        // Chuyển sang AddHikeActivity ở chế độ chỉnh sửa
        Intent intent = new Intent(HikeDetailActivity.this, AddHikeActivity.class);
        intent.putExtra("HIKE_ID", hikeId);
        startActivity(intent);
    }

    /**
     * PHƯƠNG THỨC MỚI: Chuyển sang màn hình Observations
     */
    private void goToObservations() {
        if (currentHike == null) {
            Toast.makeText(this, "Hike data is not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(HikeDetailActivity.this, ObservationActivity.class);

        // Truyền ID và Tên chuyến đi sang ObservationActivity (BẮT BUỘC cho tính năng C)
        intent.putExtra("HIKE_ID", currentHike.getId());
        intent.putExtra("HIKE_NAME", currentHike.getName());

        startActivity(intent);
    }
}