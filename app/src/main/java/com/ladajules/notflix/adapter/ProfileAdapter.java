package com.ladajules.notflix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ladajules.notflix.R;
import com.ladajules.notflix.data.model.Profile;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_PROFILE = 1;
    private static final int VIEW_TYPE_ADD = 2;

    private List<Profile> profiles = new ArrayList<>();
    private boolean canAddMore = true;
    private final OnProfileClickListener onProfileClickListener;
    private final OnAddProfileClickListener onAddProfileClickListener;

    public interface OnProfileClickListener {
        void onProfileClick(Profile profile);
    }

    public interface OnAddProfileClickListener {
        void onAddProfileClick();
    }

    public ProfileAdapter(OnProfileClickListener profileListener, OnAddProfileClickListener addListener) {
        this.onProfileClickListener = profileListener;
        this.onAddProfileClickListener = addListener;
    }

    public void submitList(List<Profile> newProfiles, int maxProfiles) {
        this.profiles = newProfiles != null ? newProfiles : new ArrayList<>();
        this.canAddMore = this.profiles.size() < maxProfiles;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return profiles.size() + (canAddMore ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return (position < profiles.size()) ? VIEW_TYPE_PROFILE : VIEW_TYPE_ADD;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PROFILE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_profile_card, parent, false);
            return new ProfileViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_add_profile_card, parent, false);
            return new AddProfileViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProfileViewHolder) {
            Profile profile = profiles.get(position);
            ((ProfileViewHolder) holder).bind(profile);
            holder.itemView.setOnClickListener(v -> onProfileClickListener.onProfileClick(profile));
        } else if (holder instanceof AddProfileViewHolder) {
            holder.itemView.setOnClickListener(v -> onAddProfileClickListener.onAddProfileClick());
        }
    }

    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivAvatar;
        private final TextView tvName;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivProfileAvatar);
            tvName = itemView.findViewById(R.id.tvProfileName);
        }

        public void bind(Profile profile) {
            tvName.setText(profile.getName());
            
            int avatarRes;
            String avatarUrl = profile.getAvatarUrl();
            
            if (avatarUrl == null) {
                avatarRes = R.drawable.avatar_default;
            } else {
                switch (avatarUrl.toLowerCase()) {
                    case "pink":
                        avatarRes = R.drawable.avatar_pink;
                        break;
                    case "green":
                        avatarRes = R.drawable.avatar_green;
                        break;
                    case "orange":
                        avatarRes = R.drawable.avatar_orange;
                        break;
                    case "yellow":
                        avatarRes = R.drawable.avatar_yellow;
                        break;
                    case "blue":
                        avatarRes = R.drawable.avatar_blue;
                        break;
                    case "default":
                    default:
                        avatarRes = R.drawable.avatar_default;
                        break;
                }
            }
            ivAvatar.setImageResource(avatarRes);
        }
    }

    static class AddProfileViewHolder extends RecyclerView.ViewHolder {
        public AddProfileViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
