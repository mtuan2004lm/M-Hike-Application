package com.example.m_hike; // Đảm bảo đúng tên package của bạn

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {

    private final Context context;
    private List<Hike> hikeList;
    private final OnHikeClickListener listener;

    public interface OnHikeClickListener {
        void onHikeClick(Hike hike);
    }

    public HikeAdapter(Context context, List<Hike> hikeList, OnHikeClickListener listener) {
        this.context = context;
        this.hikeList = hikeList;
        this.listener = listener;
    }

    public void updateData(List<Hike> newHikeList) {
        this.hikeList = newHikeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hike_item_row, parent, false);
        return new HikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        Hike hike = hikeList.get(position);

        // Gán dữ liệu cơ bản
        holder.tvHikeName.setText(hike.getName());
        holder.tvHikeLocation.setText("Location: " + hike.getLocation());
        holder.tvHikeDate.setText("Date: " + hike.getDate());

        // ✅ GÁN DỮ LIỆU MỚI: Chiều dài và Độ khó
        String lengthAndDifficulty = String.format("Length: %.1f km | Difficulty: %s",
                hike.getLength(),
                hike.getDifficultyLevel());
        holder.tvHikeLengthDifficulty.setText(lengthAndDifficulty);

        // Thiết lập sự kiện click cho toàn bộ item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHikeClick(hike);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hikeList.size();
    }

    /**
     * ViewHolder chứa các View của một mục trong danh sách.
     */
    public static class HikeViewHolder extends RecyclerView.ViewHolder {
        TextView tvHikeName;
        TextView tvHikeDate;
        TextView tvHikeLocation;
        TextView tvHikeLengthDifficulty; // ✅ Khai báo TextView mới

        public HikeViewHolder(@NonNull View itemView) {
            super(itemView);

            // ID của các TextView trong file hike_item_row.xml
            tvHikeName = itemView.findViewById(R.id.tv_hike_name);
            tvHikeDate = itemView.findViewById(R.id.tv_hike_date);
            tvHikeLocation = itemView.findViewById(R.id.tv_hike_location);
            tvHikeLengthDifficulty = itemView.findViewById(R.id.tv_hike_length_difficulty); // ✅ Ánh xạ ID mới
        }
    }
}