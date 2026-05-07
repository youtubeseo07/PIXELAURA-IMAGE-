package com.example.imagesearchapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    RecyclerView recyclerView;

    String API_KEY = "7szhDaagfzNRH2tfHQk9of9KbEpg4S10Jn8brARykjpI3kpC8ylXC4ON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.searchEdit);
        button = findViewById(R.id.searchBtn);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        button.setOnClickListener(v -> {
            String query = editText.getText().toString().trim();
            if (query.isEmpty()) {
                Toast.makeText(this, "Kuch search karo!", Toast.LENGTH_SHORT).show();
                return;
            }
            searchImages(query);
        });
    }

    private void searchImages(String query) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        apiService.searchImages(API_KEY, query, 50)
                .enqueue(new Callback<PexelsResponse>() {
                    @Override
                    public void onResponse(Call<PexelsResponse> call, Response<PexelsResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Photo> photoList = response.body().getPhotos();
                            if (photoList != null && !photoList.isEmpty()) {
                                ImageAdapter adapter = new ImageAdapter(MainActivity.this, photoList);
                                recyclerView.setAdapter(adapter);
                            } else {
                                Toast.makeText(MainActivity.this, "Koi image nahi mili!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PexelsResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
