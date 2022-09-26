package com.fi.fidemo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private ArrayList<MovieData> movieDataArrayList;
    private Activity activity;

    // Create constructor
    public MovieAdapter(Activity activity, ArrayList<MovieData> movieDataArrayList) {
        this.activity = activity;
        this.movieDataArrayList = movieDataArrayList;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Initialize view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_movie, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        // Initialize movie data
        MovieData data = movieDataArrayList.get(position);

        // Set image on image view
        Glide.with(activity).load(data.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageViewMovie);

        // Set name on text view
        holder.textViewMovie.setText(data.getName());
    }

    @Override
    public int getItemCount() {
        return movieDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewMovie;
        TextView textViewMovie;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewMovie = itemView.findViewById(R.id.image_view_movie);
            textViewMovie = itemView.findViewById(R.id.text_view_movie);
        }
    }
}
