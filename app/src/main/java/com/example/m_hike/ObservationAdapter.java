package com.example.m_hike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {

    private final Context context;
    private List<Observation> observationList;
    private OnItemClickListener listener;

    // Interface để xử lý sự kiện click từ Activity
    public interface OnItemClickListener {
        void onItemClick(Observation observation);
    }

    // Constructor
    public ObservationAdapter(Context context, List<Observation> observationList, OnItemClickListener listener) {
        this.context = context;
        this.observationList = observationList;
        this.listener = listener;
    }

    // Cập nhật danh sách mới
    public void setObservations(List<Observation> newObservations) {
        this.observationList = newObservations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ánh xạ layout item_observation.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item_observation, parent, false);
        return new ObservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        Observation currentObservation = observationList.get(position);

        // Đổ dữ liệu vào các Views
        holder.textViewObservationName.setText(currentObservation.getObservation());
        holder.textViewObservationTime.setText(currentObservation.getTimeOfTheObservation());

        String comments = currentObservation.getAdditionalComments();
        if (comments != null && !comments.isEmpty()) {
            // Hiển thị đoạn trích bình luận nếu có
            holder.textViewCommentSnippet.setText("Comments: " + comments);
        } else {
            // Ẩn nếu không có bình luận
            holder.textViewCommentSnippet.setText("No additional comments.");
        }

        // Thiết lập sự kiện click cho toàn bộ item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentObservation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return observationList.size();
    }

    // ViewHolder: Giữ các Views cho mỗi item
    public static class ObservationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewObservationName;
        TextView textViewObservationTime;
        TextView textViewCommentSnippet;

        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các Views từ item_observation.xml
            textViewObservationName = itemView.findViewById(R.id.textViewObservationName);
            textViewObservationTime = itemView.findViewById(R.id.textViewObservationTime);
            textViewCommentSnippet = itemView.findViewById(R.id.textViewCommentSnippet);

            // Có thể bỏ qua ánh xạ imageViewTimeIcon vì nó chỉ là icon trang trí
        }
    }
}