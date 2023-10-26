package com.hackaprende.dogedex.ui.dog.dogList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.hackaprende.dogedex.data.network.api.models.Dog
import com.hackaprende.dogedex.databinding.DogListItemBinding

class DogAdapter(private val onItemSelected: (Dog) -> Unit) :
    ListAdapter<Dog, DogViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<Dog>() {
        override fun areItemsTheSame(oldItem: Dog, newItem: Dog): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Dog, newItem: Dog): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DogViewHolder {
        val binding = DogListItemBinding.inflate(LayoutInflater.from(parent.context))
        return DogViewHolder(binding)
    }

    override fun onBindViewHolder(
        dogViewHolder: DogViewHolder,
        position: Int
    ) {
        val dog = getItem(position)
        dogViewHolder.bind(dog, onItemSelected)
    }
}