package com.example.pomodorolearn;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterDevelopers extends RecyclerView.Adapter<AdapterDevelopers.ViewHolder> {

    private final List<model_developer> developerList;

    // Constructor
    public AdapterDevelopers(List<model_developer> developerList) {
        this.developerList = developerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_model_developers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        model_developer developer = developerList.get(position);
        holder.bind(developer);
    }

    @Override
    public int getItemCount() {
        return developerList.size();
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView developersPhoto;
        private final TextView developersName;
        private final TextView roleDev;
        private final TextView quotesDev;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            developersPhoto = itemView.findViewById(R.id.developers_photo);
            developersName = itemView.findViewById(R.id.developers_name);
            roleDev = itemView.findViewById(R.id.role_dev);
            quotesDev = itemView.findViewById(R.id.qoutes_dev);
        }

        public void bind(model_developer developer) {
            developersPhoto.setImageResource(developer.getdevelopers_photo());
            developersName.setText(developer.getdevelopers_name());
            roleDev.setText(developer.getrole_dev());
            quotesDev.setText(developer.getqoutes_dev());
        }
    }
}
