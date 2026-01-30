package com.ladajules.notflix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ladajules.notflix.databinding.ItemHeroImageBinding

class LandingPagerAdapter : RecyclerView.Adapter<LandingPagerAdapter.PageViewHolder>() {

    // Different titles for each page
    private val pageTitles = listOf(
        "Movies, shows,\nand games in just\na few taps",
        "Download and watch\noffline",
        "No ads,\nno interruptions",
        "Watch anywhere.\nCancel anytime."
    )

    inner class PageViewHolder(val binding: ItemHeroImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding = ItemHeroImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.binding.tvHeroTitle.text = pageTitles[position]

        // Set different movie grids for variety (optional)
        // For now, all pages show the same grid
        // You can customize per position if needed
    }

    override fun getItemCount(): Int = pageTitles.size
}