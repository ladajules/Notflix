package com.ladajules.notflix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ladajules.notflix.data.model.HeroImage
import com.ladajules.notflix.databinding.ItemHeroImageBinding

class HeroImageAdapter(
    private val heroImages: List<HeroImage>
) : RecyclerView.Adapter<HeroImageAdapter.HeroViewHolder>() {

    inner class HeroViewHolder(val binding: ItemHeroImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        // Inflate the layout using the binding class.
        val binding = ItemHeroImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HeroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val item = heroImages[position]

        // Access views directly from the binding object.
        holder.binding.ivHeroImage.setImageResource(item.imageResId)
        holder.binding.tvHeroTitle.text = item.title
    }

    override fun getItemCount(): Int = heroImages.size
}