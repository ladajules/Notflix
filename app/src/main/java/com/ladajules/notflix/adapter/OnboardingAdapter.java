package com.ladajules.notflix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ladajules.notflix.R;
import com.ladajules.notflix.data.model.OnboardingItem;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    private final List<OnboardingItem> onboardingItems;

    public OnboardingAdapter(List<OnboardingItem> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_onboarding, parent, false);
        return new OnboardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        OnboardingItem item = onboardingItems.get(position);

        holder.imageView.setImageResource(item.getImageResId());
        holder.titleTextView.setText(item.getTitle());
        holder.descriptionTextView.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    public static class OnboardingViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView titleTextView;
        final TextView descriptionTextView;

        public OnboardingViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.ivOnboarding);
            titleTextView = view.findViewById(R.id.tvOnboardingTitle);
            descriptionTextView = view.findViewById(R.id.tvOnboardingDescription);
        }
    }
}
