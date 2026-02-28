package com.hackaprende.dogedex.ui.dog.dogList.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hackaprende.dogedex.R
import com.hackaprende.dogedex.data.network.api.models.Dog
import com.hackaprende.dogedex.databinding.DogListItemBinding
import com.hackaprende.dogedex.utils.loadImageCoin

class DogViewHolder(private val binding: DogListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(dog: Dog, onItemSelected: (Dog) -> Unit, onLongItemClickListener: (Dog) -> Unit) {
        if (dog.inCollection) {
            binding.dogListItemLayout.background = ContextCompat.getDrawable(
                binding.dogImage.context,
                R.drawable.dog_list_item_background
            )
            binding.dogImage.visibility = View.VISIBLE
            binding.dogIndex.visibility = View.GONE

            binding.dogListItemLayout.setOnClickListener {
                onItemSelected?.invoke(dog)
            }
            binding.dogImage.loadImageCoin(dog.imageUrl)
        } else {
            binding.dogImage.visibility = View.GONE
            binding.dogIndex.visibility = View.VISIBLE
            binding.dogIndex.text = dog.index.toString()
            binding.dogListItemLayout.background = ContextCompat.getDrawable(
                binding.dogImage.context,
                R.drawable.dog_list_item_null_background
            )
            binding.dogListItemLayout.setOnLongClickListener {
                onLongItemClickListener?.invoke(dog)
                true
            }
        }

    }
}