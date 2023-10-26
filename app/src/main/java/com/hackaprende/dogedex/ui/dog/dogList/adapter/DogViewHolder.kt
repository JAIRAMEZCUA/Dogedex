package com.hackaprende.dogedex.ui.dog.dogList.adapter

import androidx.recyclerview.widget.RecyclerView
import com.hackaprende.dogedex.data.network.api.models.Dog
import com.hackaprende.dogedex.databinding.DogListItemBinding

class DogViewHolder(private val binding: DogListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(dog: Dog, onItemSelected: (Dog) -> Unit) {
        binding.dogName.text = dog.name
        binding.dogName.setOnClickListener {
            onItemSelected.invoke(dog)
        }
    }
}