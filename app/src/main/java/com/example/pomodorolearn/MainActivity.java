package com.example.pomodorolearn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AdapterFeature adapterFeature;
    private AdapterDevelopers adapterDevelopers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi daftar fitur
        List<model_feature> listFeature = new ArrayList<>();
        listFeature.add(new model_feature(
                "Teknologi Terdepan",
                "Pomodoro Learn menggabungkan konsep Pomodoro Technique dengan teknologi modern untuk memberikan pengalaman belajar yang terdepan dan efektif."
        ));
        listFeature.add(new model_feature(
                "Optimalkan Waktu Belajar",
                "Dengan menggunakan metode 'Pomodoro', Pomodoro Learn membantu anda mengatur waktu belajar anda secara efisien, memaksimalkan fokus anda dalam sesi-sesi singkat yang terstruktur."
        ));
        listFeature.add(new model_feature(
                "Fleksibilitas Pengaturan Waktu",
                "Pomodoro Learn memungkinkan anda untuk menyesuaikan jangka waktu belajar dan istirahat sesuai dengan preferensi dan kebutuhan anda, memberikan kontrol penuh atas jadwal belajar anda."
        ));
        listFeature.add(new model_feature(
                "Nonaktifkan Alarm yang Disesuaikan",
                "Dapatkan notifikasi alarm yang dapat disesuaikan untuk memandu anda melalui setiap sesi belajar dan istirahat, membantu anda tetap terorganisir dan fokus."
        ));

        // Inisialisasi adapter untuk fitur
        adapterFeature = new AdapterFeature(listFeature);

        // Inisialisasi RecyclerView untuk fitur
        RecyclerView recyclerViewFeature = findViewById(R.id.recyclerview_feature);
        recyclerViewFeature.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewFeature.setAdapter(adapterFeature);

        // Inisialisasi RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview_feature);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(adapterDevelopers);

        // Tombol "Mari Belajar"
        Button startButton = findViewById(R.id.but_start);
        startButton.setOnClickListener(v -> {
            // Pindah ke LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        // Inisialisasi daftar developer
        List<model_developer> listDevelopers = new ArrayList<>();
        listDevelopers.add(new model_developer("Zafif Cuy", "UI/UX Designer", "Saya bangga menjadi bagian dari tim yang menciptakan PodomoroLearn untuk membantu Anda mencapai potensi belajar Anda yang sebenarnya. Semoga alat ini membantu Anda menjadi lebih fokus dan produktif dalam perjalanan belajar Anda!", R.drawable.lopek));
        listDevelopers.add(new model_developer("Zaka gyatt", "Fullstack Engineer", "Halo saya Fullstack Engineer! Kami berupaya keras untuk memastikan bahwa setiap fitur di Podomoro Learn berjalan lancar dari sisi teknis. Semoga pengalaman Anda menggunakan aplikasi kami terasa mulus dan menyenangkan. Jika Anda menemui masalah teknis, jangan ragu untuk memberi tahu kami!", R.drawable.lopek));
        listDevelopers.add(new model_developer("Huda yuhuu", "Fullstack Engineer", "Halo saya Fullstack Engineer! Kami berupaya keras untuk memastikan bahwa setiap fitur di Podomoro Learn berjalan lancar dari sisi teknis. Semoga pengalaman Anda menggunakan aplikasi kami terasa mulus dan menyenangkan. Jika Anda menemui masalah teknis, jangan ragu untuk memberi tahu kami!", R.drawable.lopek));
        listDevelopers.add(new model_developer("Tanu uu", "Front-end Developer", "Hai! Saya dari Tim Front-end bertanggung jawab menciptakan tampilan dan interaksi yang nyaman di PodomoroLearn. Semoga antarmuka yang kami rancang membantu Anda belajar lebih efisien dan menyenangkan!", R.drawable.lopek));

        // Inisialisasi adapter untuk developer
        adapterDevelopers = new AdapterDevelopers(listDevelopers);

        // Inisialisasi RecyclerView untuk developer
        RecyclerView recyclerViewDevelopers = findViewById(R.id.recyclerview_developers);
        recyclerViewDevelopers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDevelopers.setAdapter(adapterDevelopers);
    }
}
