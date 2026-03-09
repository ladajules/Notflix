package com.ladajules.notflix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ladajules.notflix.R
import com.ladajules.notflix.databinding.ItemHeroImageBinding

class LandingPagerAdapter : RecyclerView.Adapter<LandingPagerAdapter.PageViewHolder>() {

    // Different titles for each page
    private val pageTitles = listOf(
        "Movies, shows,\nand games in just\na few taps",
        "Download and watch\noffline",
        "No ads,\nno interruptions",
        "Watch anywhere.\nCancel anytime."
    )

    // Different images for each page - using onboarding_2 and onboarding_3
    private val pageImages = listOf(
        R.drawable.onboarding_2,
        R.drawable.onboarding_3,
        R.drawable.onboarding_2,
        R.drawable.onboarding_3
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
        holder.binding.ivHeroImage.setImageResource(pageImages[position])
    }

    override fun getItemCount(): Int = pageTitles.size
}