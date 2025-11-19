package com.example.m_hike;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ObservationActivity extends AppCompatActivity
        implements ObservationAdapter.OnItemClickListener {

    private HikeDatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private ObservationAdapter observationAdapter;
    private List<Observation> observationList;
    private TextView textViewHikeTitle;

    private int hikeId;
    private String hikeName;

    // Sử dụng ActivityResultLauncher để nhận kết quả từ AddObservationActivity
    private final ActivityResultLauncher<Intent> observationResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // Nếu dữ liệu đã được thêm/sửa/xóa, làm mới danh sách
                            loadObservations();
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Đảm bảo bạn đã có file activity_observations.xml
        setContentView(R.layout.activity_observations);

        dbHelper = new HikeDatabaseHelper(this);

        textViewHikeTitle = findViewById(R.id.textViewHikeTitle);
        recyclerView = findViewById(R.id.recyclerViewObservations);
        FloatingActionButton fabAddObservation = findViewById(R.id.fabAddObservation);

        // 1. Lấy dữ liệu ID chuyến đi từ HikeDetailActivity
        hikeId = getIntent().getIntExtra("HIKE_ID", -1);
        hikeName = getIntent().getStringExtra("HIKE_NAME");

        if (hikeId != -1) {
            setTitle("Observations");
            textViewHikeTitle.setText(String.format("Observations for: %s", hikeName));

            // 2. Thiết lập RecyclerView
            observationList = new ArrayList<>();
            observationAdapter = new ObservationAdapter(this, observationList, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(observationAdapter);

            // 3. Tải danh sách Observations ban đầu
            loadObservations();

            // 4. Thiết lập sự kiện cho nút FAB
            fabAddObservation.setOnClickListener(v -> goToAddObservation(null));
        } else {
            Toast.makeText(this, "Error: Hike ID not found.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void loadObservations() {
        if (hikeId != -1) {
            // Lấy danh sách Observation từ DatabaseHelper
            List<Observation> newObservations = dbHelper.getAllObservationsForHike(hikeId);

            // Cập nhật dữ liệu cho Adapter
            observationList.clear();
            observationList.addAll(newObservations);
            observationAdapter.notifyDataSetChanged();

            // Có thể hiển thị thông báo nếu danh sách trống
            if (newObservations.isEmpty()) {
                // TODO: Thêm TextView "No observations yet" và hiển thị/ẩn nó
            }
        }
    }

    private void goToAddObservation(Observation observation) {
        Intent intent = new Intent(ObservationActivity.this, AddObservationActivity.class);
        intent.putExtra("HIKE_ID", hikeId);
        intent.putExtra("HIKE_NAME", hikeName);

        if (observation != null) {
            // Chuyển sang chế độ EDIT
            intent.putExtra("OBSERVATION_ID", observation.getId());
            // TODO: Nếu bạn đã tạo phương thức getObservation(int id) trong DB, bạn có thể truyền ID
        }

        observationResultLauncher.launch(intent);
    }

    // Xử lý sự kiện click vào một item (từ interface ObservationAdapter.OnItemClickListener)
    @Override
    public void onItemClick(Observation observation) {
        // Mở AddObservationActivity ở chế độ EDIT
        goToAddObservation(observation);
    }
}