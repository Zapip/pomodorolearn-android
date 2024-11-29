package com.example.pomodorolearn;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterFeature extends RecyclerView.Adapter<AdapterFeature.ViewHolder> {

    private final List<model_feature> items;

    // Constructor
    public AdapterFeature(List<model_feature> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_model_feature, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder Class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvJudulFeature;
        private final TextView tvDescriptionFeature;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudulFeature = itemView.findViewById(R.id.tv_judul_feature);
            tvDescriptionFeature = itemView.findViewById(R.id.tv_description_feature);
        }

        public void bind(model_feature data) {
            tvJudulFeature.setText(data.gettv_judul_feature());
            tvDescriptionFeature.setText(data.gettv_description_feature());
        }
    }
}
