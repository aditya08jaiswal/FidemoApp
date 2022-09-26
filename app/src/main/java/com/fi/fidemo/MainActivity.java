package com.fi.fidemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
//    public static final String API_KEY = "c26e8db1";
//    public static final String BASE_URL = "http://www.omdbapi.com/";

    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<MovieData> movieDataArrayList = new ArrayList<>();
    MovieAdapter movieAdapter;
    int page = 1, limit=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.recycler_view_movie);
        progressBar = findViewById(R.id.progress_bar);

        // Initialize Adapter
        movieAdapter = new MovieAdapter(MainActivity.this, movieDataArrayList);
        // Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Set adapter
        recyclerView.setAdapter(movieAdapter);

        // Create get data method
        getData(page, limit);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Check condition
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // When reach last item position
                    // Increase page size
                    page++;
                    // Show progress bar
                    progressBar.setVisibility(View.VISIBLE);
                    // Call Method
                    getData(page, limit);
                }
            }
        });
    }

    private void getData(int page, int limit) {

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
                .baseUrl("https://picsum.photos")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        // Create movie interface
        MovieInterface movieInterface = retrofit.create(MovieInterface.class);

        // Initialize call
        Call<String> call = movieInterface.STRING_CALL(page, limit);
//        Call<String> call = movieInterface.setAPIKey(API_KEY);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // Check condition
                if (response.isSuccessful() && response.body() != null){
                    // When response is successful and not empty
                    // Hide progress bar
                    progressBar.setVisibility(View.GONE);
                    try {
                        // Initialize JSON Array
                        JSONArray jsonArray = new JSONArray(response.body());
                        // Parse JSON Array
                        parseResult(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

    }

    private void parseResult(JSONArray jsonArray) {
        // Use for loop
        for (int i=0; i<jsonArray.length(); i++) {
            try {
                // Initialize JSON Object
                JSONObject object = jsonArray.getJSONObject(i);
                Log.i("OBJECT --> ", object.toString());
                // Initialize movie data
                MovieData data = new MovieData();
                // Set image
                data.setImage(object.getString("download_url"));
                // Set name
                data.setName(object.getString("author"));
                // Add data in array list
                movieDataArrayList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Initialize adapter
            movieAdapter = new MovieAdapter(MainActivity.this, movieDataArrayList);
            // Set adapter
            recyclerView.setAdapter(movieAdapter);
        }
    }
}