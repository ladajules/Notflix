package com.ladajules.notflix.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ladajules.notflix.data.model.Download;
import com.ladajules.notflix.databinding.ItemDownloadedBinding;

import java.util.ArrayList;
import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {

    private List<Download> downloads = new ArrayList<>();
    private final OnDownloadClickListener listener;

    public interface OnDownloadClickListener {
        void onDeleteClick(Download download);
        void onItemClick(Download download);
    }

    public DownloadAdapter(OnDownloadClickListener listener) {
        this.listener = listener;
    }

    public void setDownloads(List<Download> downloads) {
        this.downloads = downloads != null ? downloads : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDownloadedBinding binding = ItemDownloadedBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new DownloadViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, int position) {
        Download download = downloads.get(position);
        holder.bind(download, listener);
    }

    @Override
    public int getItemCount() {
        return downloads.size();
    }

    static class DownloadViewHolder extends RecyclerView.ViewHolder {
        private final ItemDownloadedBinding binding;

        public DownloadViewHolder(ItemDownloadedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Download download, OnDownloadClickListener listener) {
            binding.tvTitle.setText(download.getTitle());
            binding.tvSubtitle.setText(download.getSize());
            binding.tvStatus.setText(download.getStatus());

            Glide.with(itemView.getContext())
                    .load(download.getFullBackdropPath())
                    .into(binding.ivThumbnail);

            binding.ivDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(download);
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(download);
                }
            });
        }
    }
}
