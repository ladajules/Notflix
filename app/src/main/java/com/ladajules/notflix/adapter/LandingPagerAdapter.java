package com.ladajules.notflix.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ladajules.notflix.R;
import com.ladajules.notflix.databinding.ItemHeroImageBinding;

import java.util.Arrays;
import java.util.List;

public class LandingPagerAdapter extends RecyclerView.Adapter<LandingPagerAdapter.PageViewHolder> {

    // Different titles for each page
    private final List<String> pageTitles = Arrays.asList(
            "Movies, shows,\nand games in just\na few taps",
            "Download and watch\noffline",
            "No ads,\nno interruptions",
            "Watch anywhere.\nCancel anytime."
    );

    // Different images for each page
    private final List<Integer> pageImages = Arrays.asList(
            R.drawable.onboarding_2,
            R.drawable.onboarding_3,
            R.drawable.onboarding_2,
            R.drawable.onboarding_3
    );

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHeroImageBinding binding = ItemHeroImageBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new PageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        holder.binding.tvHeroTitle.setText(pageTitles.get(position));
        holder.binding.ivHeroImage.setImageResource(pageImages.get(position));
    }

    @Override
    public int getItemCount() {
        return pageTitles.size();
    }

    public static class PageViewHolder extends RecyclerView.ViewHolder {
        final ItemHeroImageBinding binding;

        public PageViewHolder(ItemHeroImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
