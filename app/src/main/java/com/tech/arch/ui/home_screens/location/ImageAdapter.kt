package com.tech.arch.ui.home_screens.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tech.arch.R


class ImageAdapter(private val imageUrls: List<String>) :
        RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

        inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imageView: ImageView = view.findViewById(R.id.mapImage)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image, parent, false)
            return ImageViewHolder(view)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            Glide.with(holder.imageView.context)
                .load(imageUrls[position])
                .into(holder.imageView)
        }

        override fun getItemCount(): Int = imageUrls.size
    }
