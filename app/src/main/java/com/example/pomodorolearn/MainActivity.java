package com.example.pomodorolearn;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AdapterFeature adapterModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi daftar model
        List<model_feature> listModel = new ArrayList<>();
        listModel.add(new model_feature(
                "Teknologi Terdepan",
                "Pomodoro Learn menggabungkan konsep Pomodoro Technique dengan teknologi modern untuk memberikan pengalaman belajar yang terdepan dan efektif."
        ));
        listModel.add(new model_feature(
                "Optimalkan Waktu Belajar",
                "Dengan menggunakan metode 'Pomodoro', Pomodoro Learn membantu anda mengatur waktu belajar anda secara efisien, memaksimalkan fokus anda dalam sesi-sesi singkat yang terstruktur."
        ));
        listModel.add(new model_feature(
                "Fleksibilitas Pengaturan Waktu",
                "Pomodoro Learn memungkinkan anda untuk menyesuaikan jangka waktu belajar dan istirahat sesuai dengan preferensi dan kebutuhan anda, memberikan kontrol penuh atas jadwal belajar anda."
        ));
        listModel.add(new model_feature(
                "Nonaktifkan Alarm yang Disesuaikan",
                "Dapatkan notifikasi alarm yang dapat disesuaikan untuk memandu anda melalui setiap sesi belajar dan istirahat, membantu anda tetap terorganisir dan fokus."
        ));

        // Inisialisasi Adapter
        adapterModel = new AdapterFeature(listModel);

        // Inisialisasi RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview_feature);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(adapterModel);
    }
}
