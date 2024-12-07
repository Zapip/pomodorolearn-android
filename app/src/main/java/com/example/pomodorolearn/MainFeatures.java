package com.example.pomodorolearn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;

public class MainFeatures extends AppCompatActivity {
    private TextView title;
    private static final String API_URL = "https://api.api-ninjas.com/v1/quotes?category=learning";
    private static final String API_KEY = "wuGfnqoGKMi0CWNsW3dcuQ==fNF2iSr8T9H2fQMY";

    private TextView timerDisplay, btnPomodoro, btnShortBreak, btnLongBreak;
    private Button btnPause;
    private ImageButton btnSkip;

    private CountDownTimer timer;
    private boolean isTimerRunning = false;
    private boolean isPomodoroActive = true;
    private boolean isShortBreakActive = false;
    private boolean isLongBreakActive = false;

    private final int pomodoroTime = 25 * 60 * 1000; // 25 minutes in milliseconds
    private final int shortBreakTime = 5 * 60 * 1000; // 5 minutes in milliseconds
    private final int longBreakTime = 15 * 60 * 1000; // 15 minutes in milliseconds
    private int currentInterval = 0; // Number of completed Pomodoro sessions
    private int currentTimeLeft = pomodoroTime;

    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_features);

        db = FirebaseFirestore.getInstance();

        // Inisialisasi RecyclerView
        taskRecyclerView = findViewById(R.id.task_recycler_view);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);
        taskRecyclerView.setAdapter(taskAdapter);

        findViewById(R.id.add_task_button).setOnClickListener(v -> {
            // Membuat EditText untuk input tugas
            EditText input = new EditText(MainFeatures.this);
            input.setHint("Input task here");

            // Membuat AlertDialog untuk menambahkan tugas baru
            AlertDialog.Builder builder = new AlertDialog.Builder(MainFeatures.this);
            builder.setTitle("Add New Task")
                    .setView(input)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String taskTitle = input.getText().toString().trim();
                            if (!taskTitle.isEmpty()) {
                                // Instance Firestore
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                // Generate ID untuk dokumen baru
                                String generatedId = db.collection("tasks").document().getId();

                                // Membuat objek Task dengan ID
                                Task newTask = new Task(generatedId, taskTitle, false);

                                // Menyimpan data ke Firestore
                                db.collection("tasks")
                                        .document(generatedId) // Menggunakan ID yang di-generate
                                        .set(newTask) // Menyimpan objek Task ke dokumen
                                        .addOnSuccessListener(aVoid -> {
                                            // Setelah data berhasil disimpan
                                            taskList.add(newTask); // Tambahkan tugas ke daftar lokal
                                            taskAdapter.notifyItemInserted(taskList.size() - 1); // Perbarui RecyclerView
                                            System.out.println("Task added with ID: " + generatedId);
                                        })
                                        .addOnFailureListener(e -> {
                                            // Jika terjadi kesalahan saat menyimpan
                                            System.err.println("Error adding task: " + e.getMessage());
                                        });
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        });


        fetchTasksFromFirestore();

        // Initialize views
        timerDisplay = findViewById(R.id.timer_display);
        btnPomodoro = findViewById(R.id.btn_pomodoro);
        btnShortBreak = findViewById(R.id.btn_short_break);
        btnLongBreak = findViewById(R.id.btn_long_break);
        btnPause = findViewById(R.id.btn_pause);
        btnSkip = findViewById(R.id.btn_skip);
        title = findViewById(R.id.title);

        // Set initial states
        updateTimerDisplay(pomodoroTime);
        btnPause.setText("Mulai");
        setActiveButton(btnPomodoro); // Highlight tombol Pomodoro
        updateTitleBasedOnState(); // Ambil quote dari API jika waktu belum berjalan

        // Pause/Start button functionality
        btnPause.setOnClickListener(view -> {
            if (isTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        // Skip button functionality
        btnSkip.setOnClickListener(view -> skipTimer());
    }

    private void fetchTasksFromFirestore() {
        db.collection("tasks")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (DocumentSnapshot document : querySnapshot) {
                                String id = document.getId();
                                String title = document.getString("title");
                                boolean isCompleted = document.getBoolean("is_complete") != null
                                        ? document.getBoolean("is_complete")
                                        : false;

                                // Tambahkan tugas ke daftar
                                taskList.add(new Task(id,title, isCompleted));
                            }
                            taskAdapter.notifyDataSetChanged(); // Perbarui RecyclerView
                        }
                    } else {
                        System.err.println("Error fetching tasks: " + task.getException());
                    }
                });
    }

    private void startTimer() {
        btnPause.setText("Pause");
        isTimerRunning = true;
        updateTitleBasedOnState(); // Update title berdasarkan state aktif

        timer = new CountDownTimer(currentTimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentTimeLeft = (int) millisUntilFinished;
                updateTimerDisplay(currentTimeLeft);
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                currentTimeLeft = 0;
                updateTimerDisplay(currentTimeLeft);
                handleTimerCompletion();
            }
        }.start();
    }

    private void pauseTimer() {
        if (timer != null) {
            timer.cancel();
        }
        isTimerRunning = false;
        btnPause.setText("Mulai");
    }

    private void skipTimer() {
        if (timer != null) {
            timer.cancel();
        }
        isTimerRunning = false;
        handleTimerCompletion();
    }

    private void handleTimerCompletion() {
        if (isPomodoroActive) {
            currentInterval++;
            if (currentInterval >= 4) {
                // Switch to Long Break after 4 Pomodoro intervals
                isPomodoroActive = false;
                isShortBreakActive = false;
                isLongBreakActive = true;
                currentTimeLeft = longBreakTime;
                setActiveButton(btnLongBreak);
            } else {
                // Switch to Short Break after each Pomodoro
                isPomodoroActive = false;
                isShortBreakActive = true;
                isLongBreakActive = false;
                currentTimeLeft = shortBreakTime;
                setActiveButton(btnShortBreak);
            }
        } else if (isShortBreakActive) {
            // Reset to Pomodoro after Short Break
            isPomodoroActive = true;
            isShortBreakActive = false;
            isLongBreakActive = false;
            currentTimeLeft = pomodoroTime;
            setActiveButton(btnPomodoro);
        } else if (isLongBreakActive) {
            // Reset to Pomodoro and restart the cycle after Long Break
            isPomodoroActive = true;
            isShortBreakActive = false;
            isLongBreakActive = false;
            currentInterval = 0; // Reset interval count for a new cycle
            currentTimeLeft = pomodoroTime;
            setActiveButton(btnPomodoro);
        }
        updateTimerDisplay(currentTimeLeft);
        btnPause.setText("Mulai");
        updateTitleBasedOnState();
    }

    private void setActiveButton(TextView activeButton) {
        // Reset all buttons to default color
        btnPomodoro.setTextColor(getResources().getColor(android.R.color.white));
        btnShortBreak.setTextColor(getResources().getColor(android.R.color.white));
        btnLongBreak.setTextColor(getResources().getColor(android.R.color.white));

        // Highlight active button
        if (activeButton == btnPomodoro) {
            btnPomodoro.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else if (activeButton == btnShortBreak) {
            btnShortBreak.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
        } else if (activeButton == btnLongBreak) {
            btnLongBreak.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    private void updateTimerDisplay(int millis) {
        int minutes = (millis / 1000) / 60;
        int seconds = (millis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerDisplay.setText(timeFormatted);
    }

    private void updateTitleBasedOnState() {
        if (!isTimerRunning) {
            if (isPomodoroActive) {
                // Ambil quote dari API
                new GetQuoteTask().execute(API_URL);
            }
        } else {
            if (isPomodoroActive) {
                title.setText("Saatnya Fokus");
                title.setTextSize(20);
            } else if (isShortBreakActive) {
                title.setText("Istirahat Sejenak");
                title.setTextSize(20);
            } else if (isLongBreakActive) {
                title.setText("Istirahat Panjang");
                title.setTextSize(20);
            }
        }
    }

    private class GetQuoteTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestProperty("X-Api-Key", API_KEY); // Menambahkan header API key

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();

                JSONArray responseArray = new JSONArray(stringBuilder.toString());
                JSONObject firstQuote = responseArray.getJSONObject(0);
                result = firstQuote.getString("quote");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String quote) {
            if (quote != null && !quote.isEmpty()) {
                title.setText("“" + quote + "”");
                title.setTextSize(15);
            } else {
                title.setText("No quote available.");
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }
}