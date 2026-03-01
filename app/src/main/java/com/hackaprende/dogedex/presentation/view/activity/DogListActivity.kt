package com.hackaprende.dogedex.presentation.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackaprende.dogedex.databinding.ActivityDogListBinding
import com.hackaprende.dogedex.presentation.view.adapter.DogAdapter
import com.hackaprende.dogedex.presentation.viewmodel.DogListViewModel

class DogListActivity : AppCompatActivity() {

    private val dogListViewModel: DogListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //recycler view setup
        val recycler = binding.dogRecycler
        recycler.layoutManager = LinearLayoutManager(this)

        //adapter setup
        val adapter = DogAdapter()
        recycler.adapter = adapter

        //observe the dog list and submit it to the adapter
        dogListViewModel.dogList.observe(this) {
            dogList ->
            adapter.submitList(dogList)
        }
    }
}