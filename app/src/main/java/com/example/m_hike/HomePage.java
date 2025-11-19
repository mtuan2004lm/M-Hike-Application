package com.example.m_hike;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomePage extends AppCompatActivity implements HikeAdapter.OnHikeClickListener {

    private RecyclerView recyclerViewHikes;
    private HikeAdapter hikeAdapter;
    private Button buttonAddHike;
    private HikeDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // 1. Ánh xạ View
        recyclerViewHikes = findViewById(R.id.recyclerViewHikes);
        buttonAddHike = findViewById(R.id.button3);

        // Khởi tạo Database Helper
        dbHelper = new HikeDatabaseHelper(this);

        // 2. Thiết lập RecyclerView
        recyclerViewHikes.setLayoutManager(new LinearLayoutManager(this));

        // 3. Xử lý sự kiện Thêm chuyến đi mới
        buttonAddHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, AddHikeActivity.class);
                startActivity(intent);
            }
        });

        // Gọi loadHikes lần đầu để thiết lập Adapter
        loadHikes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật dữ liệu mỗi khi Activity trở lại foreground
        loadHikes();
    }

    private void loadHikes() {
        // Tải danh sách tất cả các chuyến đi từ SQLite
        List<Hike> hikeList = dbHelper.getAllHikes();

        // Cập nhật Adapter
        if (hikeAdapter == null) {
            // Lần đầu tiên: Khởi tạo Adapter.
            // Truyền 'this' (là HomePage) làm OnHikeClickListener
            hikeAdapter = new HikeAdapter(this, hikeList, this);
            recyclerViewHikes.setAdapter(hikeAdapter);
        } else {
            // Các lần sau: Chỉ cần cập nhật dữ liệu
            hikeAdapter.updateData(hikeList);
        }
    }

    // 4. Triển khai phương thức click từ HikeAdapter.OnHikeClickListener
    @Override
    public void onHikeClick(Hike hike) {
        // Chuyển sang màn hình Chi tiết/Chỉnh sửa chuyến đi (Hike Detail Activity)
        Intent intent = new Intent(HomePage.this, HikeDetailActivity.class);
        intent.putExtra("HIKE_ID", hike.getId());
        startActivity(intent);
    }
}