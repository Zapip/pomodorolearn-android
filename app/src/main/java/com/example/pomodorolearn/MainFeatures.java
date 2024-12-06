package com.example.pomodorolearn;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainFeatures extends AppCompatActivity {
    private TextView title;
    private static final String API_URL = "https://api.api-ninjas.com/v1/quotes?category=learning";
    private static final String API_KEY = "wuGfnqoGKMi0CWNsW3dcuQ==fNF2iSr8T9H2fQMY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_features);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainFeatures), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        title = findViewById(R.id.title);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new GetQuoteTask().execute(API_URL);
                handler.postDelayed(this, 60000); // 45 seconds
            }
        };
        handler.post(runnable);
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

                // Parse the response string as JSON
                JSONArray responseArray = new JSONArray(stringBuilder.toString());
                // Ambil quote pertama dalam array
                JSONObject firstQuote = responseArray.getJSONObject(0);
                result = firstQuote.getString("quote");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String quote) {
            super.onPostExecute(quote);
            // Set the quote to the TextView
            if (quote != null && !quote.isEmpty()) {
                title.setText("“"+quote+"”");
                title.setTextSize(12);
            } else {
                title.setText("No quote available.");
            }
        }
    }
}