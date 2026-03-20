package com.ladajules.notflix.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ladajules.notflix.data.model.HeroImage;
import com.ladajules.notflix.databinding.ItemHeroImageBinding;

import java.util.List;

public class HeroImageAdapter extends RecyclerView.Adapter<HeroImageAdapter.HeroViewHolder> {

    private final List<HeroImage> heroImages;

    public HeroImageAdapter(List<HeroImage> heroImages) {
        this.heroImages = heroImages;
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout using the binding class.
        ItemHeroImageBinding binding = ItemHeroImageBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new HeroViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, int position) {
        HeroImage item = heroImages.get(position);

        // Access views directly from the binding object.
        holder.binding.ivHeroImage.setImageResource(item.getImageResId());
        holder.binding.tvHeroTitle.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return heroImages != null ? heroImages.size() : 0;
    }

    public static class HeroViewHolder extends RecyclerView.ViewHolder {
        final ItemHeroImageBinding binding;

        public HeroViewHolder(ItemHeroImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
