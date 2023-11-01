package com.hackaprende.dogedex.ui.dog.dogList.adapter

import androidx.recyclerview.widget.RecyclerView
import com.hackaprende.dogedex.data.network.api.models.Dog
import com.hackaprende.dogedex.databinding.DogListItemBinding
import com.hackaprende.dogedex.utils.loadImageCoin

class DogViewHolder(private val binding: DogListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(dog: Dog, onItemSelected: (Dog) -> Unit, onLongItemClickListener: (Dog) -> Unit) {
        binding.dogImage.loadImageCoin(dog.imageUrl)
        binding.dogImage.setOnClickListener {
            onItemSelected.invoke(dog)
        }
        binding.dogImage.setOnLongClickListener {
            onLongItemClickListener.invoke(dog)
            true
        }
    }
}