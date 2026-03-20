package com.ladajules.notflix.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ladajules.notflix.R;
import com.ladajules.notflix.data.model.Movie;
import com.ladajules.notflix.databinding.ItemMoviePosterBinding;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    private final OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieAdapter(OnMovieClickListener listener) {
        this.listener = listener;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies != null ? movies : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMoviePosterBinding binding = ItemMoviePosterBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie, listener);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ItemMoviePosterBinding binding;

        public MovieViewHolder(ItemMoviePosterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Movie movie, OnMovieClickListener listener) {
            Glide.with(itemView.getContext())
                    .load(movie.getFullPosterPath())
                    .placeholder(R.drawable.profile_avatar_background) // Placeholder
                    .into(binding.ivPoster);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMovieClick(movie);
                }
            });
        }
    }
}
