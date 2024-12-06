package com.example.pomodorolearn;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.taskTitle.setText(task.getTitle());

        // Set ikon berdasarkan status selesai
        if (task.isCompleted()) {
            holder.check_circle.setImageResource(R.drawable.checklist_completed); // Ikon checklist selesai
        } else {
            holder.check_circle.setImageResource(R.drawable.check_circle); // Ikon checklist belum selesai
        }

        // Klik untuk toggle status selesai
        holder.check_circle.setOnClickListener(v -> {
            boolean newStatus = !task.isCompleted();
            task.setCompleted(newStatus);

            // Update ikon berdasarkan status baru
            if (newStatus) {
                holder.check_circle.setImageResource(R.drawable.checklist_completed);
            } else {
                holder.check_circle.setImageResource(R.drawable.check_circle);
            }

            // Perbarui di Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("tasks")
                    .document(task.getId()) // Gunakan ID unik dari tugas
                    .update("is_complete", newStatus)
                    .addOnSuccessListener(aVoid -> {
                        System.out.println("Task updated successfully in Firestore");
                    })
                    .addOnFailureListener(e -> {
                        System.err.println("Error updating task in Firestore: " + e.getMessage());
                    });
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle;
        ImageView check_circle;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.task_title);
            check_circle = itemView.findViewById(R.id.checklist);
        }
    }
}
