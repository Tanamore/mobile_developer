package com.example.mytanamore.Ensiklopedia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytanamore.databinding.ItemEnsiklopediaBinding
import com.example.mytanamore.response.DataItem

class EnsiklopediaAdapter(private val onItemClick: (DataItem) -> Unit) :
    ListAdapter<DataItem, EnsiklopediaAdapter.EnsiklopediaViewHolder>(EnsiklopediaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnsiklopediaViewHolder {
        val binding = ItemEnsiklopediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EnsiklopediaViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: EnsiklopediaViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class EnsiklopediaViewHolder(private val binding: ItemEnsiklopediaBinding,
        private val onItemClick: (DataItem) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItem) {
            Glide.with(binding.ivPlantImage.context)
                .load(item.imageUrl)
                .centerCrop()
                .into(binding.ivPlantImage)

            binding.tvPlantTitle.text = item.plantName ?: "Unknown Plant"
            binding.tvPlantDescription.text = item.description ?: "No description available"

            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    class EnsiklopediaDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }
}