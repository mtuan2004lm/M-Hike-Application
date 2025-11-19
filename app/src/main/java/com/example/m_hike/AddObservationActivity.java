package com.example.m_hike;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import java.util.Locale;

public class AddObservationActivity extends AppCompatActivity {

    private TextInputEditText editTextObservation, editTextTime, editTextComments;
    private Button buttonSave, buttonDelete;
    private TextView textViewHikeContext;
    private HikeDatabaseHelper dbHelper;

    private int hikeId = -1;
    private String hikeName;
    private int observationId = -1; // -1: Add mode, >0: Edit mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);

        dbHelper = new HikeDatabaseHelper(this);

        // 1. Ánh xạ Views
        editTextObservation = findViewById(R.id.editTextObservation);
        editTextTime = findViewById(R.id.editTextTime);
        editTextComments = findViewById(R.id.editTextComments);
        buttonSave = findViewById(R.id.buttonSaveObservation);
        buttonDelete = findViewById(R.id.buttonDeleteObservation);
        textViewHikeContext = findViewById(R.id.textViewHikeContext);

        // 2. Lấy dữ liệu từ Intent
        getIntentData();

        // 3. Thiết lập tiêu đề và chế độ
        setupMode();

        // 4. Thiết lập sự kiện
        editTextTime.setOnClickListener(v -> showTimePicker());
        buttonSave.setOnClickListener(v -> saveObservation());
        buttonDelete.setOnClickListener(v -> deleteObservation());
    }

    private void getIntentData() {
        // Lấy ID và Tên chuyến đi từ HikeDetailActivity (luôn có)
        hikeId = getIntent().getIntExtra("HIKE_ID", -1);
        hikeName = getIntent().getStringExtra("HIKE_NAME");

        // Lấy ID Quan sát nếu ở chế độ chỉnh sửa (tùy chọn)
        observationId = getIntent().getIntExtra("OBSERVATION_ID", -1);
    }

    private void setupMode() {
        if (hikeId == -1) {
            Toast.makeText(this, "Error: Hike ID not found.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Cập nhật TextView hiển thị tên chuyến đi
        textViewHikeContext.setText(String.format("Adding Observation for: %s", hikeName));

        if (observationId != -1) {
            // Chế độ CHỈNH SỬA
            setTitle("Edit Observation");
            buttonSave.setText("UPDATE OBSERVATION");
            buttonDelete.setVisibility(Button.VISIBLE);

            // Tải dữ liệu cũ của Observation để hiển thị
            loadObservationData(observationId);

        } else {
            // Chế độ THÊM MỚI
            setTitle("Add New Observation");
            // Mặc định thời gian hiện tại
            setCurrentTime();
        }
    }

    private void loadObservationData(int obsId) {
        // TODO: Cần tạo phương thức getObservation(int id) trong HikeDatabaseHelper
        // Vì hiện tại chúng ta chưa có phương thức đó, ta sẽ giả định nó đã tồn tại:
        // Observation obs = dbHelper.getObservation(obsId);
        // if (obs != null) {
        //     editTextObservation.setText(obs.getObservation());
        //     editTextTime.setText(obs.getTimeOfTheObservation());
        //     editTextComments.setText(obs.getAdditionalComments());
        // } else {
        //     Toast.makeText(this, "Observation data not found.", Toast.LENGTH_SHORT).show();
        //     finish();
        // }
    }

    private void setCurrentTime() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // Định dạng thời gian HH:MM
        String timeStr = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
        editTextTime.setText(timeStr);
    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, h, m) -> {
                    String timeStr = String.format(Locale.getDefault(), "%02d:%02d", h, m);
                    editTextTime.setText(timeStr);
                }, hour, minute, true); // true cho định dạng 24h
        timePickerDialog.show();
    }

    private void saveObservation() {
        String observationName = editTextObservation.getText().toString().trim();
        String time = editTextTime.getText().toString().trim();
        String comments = editTextComments.getText().toString().trim();

        // 1. Xác thực trường bắt buộc
        if (observationName.isEmpty()) {
            editTextObservation.setError("Observation is required.");
            return;
        }
        if (time.isEmpty()) {
            editTextTime.setError("Time is required.");
            return;
        }

        // 2. Tạo đối tượng Observation
        Observation observation = new Observation();
        observation.setHikeId(hikeId);
        observation.setObservation(observationName);
        observation.setTimeOfTheObservation(time);
        observation.setAdditionalComments(comments);

        long result;
        if (observationId == -1) {
            // THÊM MỚI
            result = dbHelper.addObservation(observation);
            if (result > 0) {
                Toast.makeText(this, "Observation added successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add observation.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // CHỈNH SỬA
            observation.setId(observationId);
            result = dbHelper.updateObservation(observation);
            if (result > 0) {
                Toast.makeText(this, "Observation updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update observation.", Toast.LENGTH_SHORT).show();
            }
        }

        // Trở về ObservationActivity để làm mới danh sách
        setResult(RESULT_OK);
        finish();
    }

    private void deleteObservation() {
        if (observationId != -1) {
            long result = dbHelper.deleteObservation(observationId);
            if (result > 0) {
                Toast.makeText(this, "Observation deleted successfully!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK); // Báo cho Activity trước biết đã có thay đổi
                finish();
            } else {
                Toast.makeText(this, "Failed to delete observation.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}