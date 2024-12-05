package com.example.pomodorolearn;

import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainFeatures extends AppCompatActivity {

    private TextView timerDisplay, btnPomodoro, btnShortBreak, btnLongBreak;
    private Button btnPause;
    private ImageButton btnSkip;

    private CountDownTimer timer;
    private boolean isTimerRunning = false;
    private boolean isPomodoroActive = true;
    private boolean isShortBreakActive = false;
    private boolean isLongBreakActive = false;

    private int pomodoroTime = 25 * 60 * 1000; // 25 minutes in milliseconds
    private int shortBreakTime = 5 * 60 * 1000; // 5 minutes in milliseconds
    private int longBreakTime = 15 * 60 * 1000; // 15 minutes in milliseconds
    private int currentInterval = 0; // Number of completed Pomodoro sessions
    private int currentTimeLeft = pomodoroTime; // Remaining time in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_features);

        // Initialize views
        timerDisplay = findViewById(R.id.timer_display);
        btnPomodoro = findViewById(R.id.btn_pomodoro);
        btnShortBreak = findViewById(R.id.btn_short_break);
        btnLongBreak = findViewById(R.id.btn_long_break);
        btnPause = findViewById(R.id.btn_pause);
        btnSkip = findViewById(R.id.btn_skip);

        // Set initial states
        updateTimerDisplay(pomodoroTime); // Set timer display to 25:00
        btnPause.setText("Mulai");
        setActiveButton(btnPomodoro);

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

    private void startTimer() {
        btnPause.setText("Pause");
        isTimerRunning = true;

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
                // Switch to Long Break
                isPomodoroActive = false;
                isShortBreakActive = false;
                isLongBreakActive = true;
                currentTimeLeft = longBreakTime;
                setActiveButton(btnLongBreak);
            } else {
                // Switch to Short Break
                isPomodoroActive = false;
                isShortBreakActive = true;
                isLongBreakActive = false;
                currentTimeLeft = shortBreakTime;
                setActiveButton(btnShortBreak);
            }
        } else if (isShortBreakActive || isLongBreakActive) {
            // Reset to Pomodoro
            isPomodoroActive = true;
            isShortBreakActive = false;
            isLongBreakActive = false;
            currentTimeLeft = pomodoroTime;
            setActiveButton(btnPomodoro);
        }
        updateTimerDisplay(currentTimeLeft);
        btnPause.setText("Mulai");
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

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }
}