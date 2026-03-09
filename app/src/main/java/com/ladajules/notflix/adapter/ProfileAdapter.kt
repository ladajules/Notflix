package com.ladajules.notflix.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ladajules.notflix.R
import com.ladajules.notflix.data.model.Profile

class ProfileAdapter(
    private val onProfileClick: (Profile) -> Unit,
    private val onAddProfileClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_PROFILE = 1
        private const val VIEW_TYPE_ADD = 2
    }

    private var profiles: List<Profile> = emptyList()
    private var canAddMore: Boolean = true

    fun submitList(newProfiles: List<Profile>, maxProfiles: Int = 5) {
        profiles = newProfiles
        canAddMore = newProfiles.size < maxProfiles
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        // Profiles + Add button (if can add more)
        return profiles.size + if (canAddMore) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < profiles.size) {
            VIEW_TYPE_PROFILE
        } else {
            VIEW_TYPE_ADD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_PROFILE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_profile_card, parent, false)
                ProfileViewHolder(view)
            }
            VIEW_TYPE_ADD -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_add_profile_card, parent, false)
                AddProfileViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProfileViewHolder -> {
                val profile = profiles[position]
                holder.bind(profile)
                holder.itemView.setOnClickListener { onProfileClick(profile) }
            }
            is AddProfileViewHolder -> {
                holder.itemView.setOnClickListener { onAddProfileClick() }
            }
        }
    }

    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivAvatar: ImageView = itemView.findViewById(R.id.ivProfileAvatar)
        private val tvName: TextView = itemView.findViewById(R.id.tvProfileName)

        fun bind(profile: Profile) {
            tvName.text = profile.name
            // Set avatar based on avatarUrl or use default
            val avatarRes = when (profile.avatarUrl) {
                "avatar_1" -> R.drawable.profile_avatar_background
                "avatar_2" -> R.drawable.profile_avatar_background
                "avatar_3" -> R.drawable.profile_avatar_background
                "avatar_4" -> R.drawable.profile_avatar_background
                "avatar_5" -> R.drawable.profile_avatar_background
                else -> R.drawable.profile_avatar_background
            }
            ivAvatar.setBackgroundResource(avatarRes)
        }
    }

    inner class AddProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}