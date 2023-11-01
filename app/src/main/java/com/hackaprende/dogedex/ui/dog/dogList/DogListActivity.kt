package com.hackaprende.dogedex.ui.dog.dogList

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.hackaprende.dogedex.data.network.api.models.Dog
import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatusGeneric
import com.hackaprende.dogedex.databinding.ActivityDogListBinding
import com.hackaprende.dogedex.ui.dog.dogDetail.DogDetailActivity
import com.hackaprende.dogedex.ui.dog.dogList.adapter.DogAdapter
import com.hackaprende.dogedex.utils.DOG_KEY
import com.hackaprende.dogedex.utils.GRID_SPAN_COUNT
import com.hackaprende.dogedex.utils.visible

class DogListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDogListBinding
    private val dogListViewModel: DogListViewModel by viewModels()
    private lateinit var dogAdapter: DogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initList()
        initUIState()
    }

    private fun initUIState() {
        /*
         TODO ASI SE IMPLEMENTA UN FLOW
         lifecycleScope.launch {
                  repeatOnLifecycle(Lifecycle.State.STARTED) {

                       dogListViewModel.statusDownload.collect {
                               when (it) {
                                   ApiResponseStatus.LOADING -> loadState()
                                   is ApiResponseStatus.ERROR -> errorState(it.error)
                                   is ApiResponseStatus.SUCCESS -> successState(it.dogList)
                               }
                           }
                  }
              }
      */

        dogListViewModel.status.observe(this) {
            when (it) {
                is ApiResponseStatusGeneric.ERROR -> hideLoader()
                is ApiResponseStatusGeneric.LOADING -> showLoader()
                is ApiResponseStatusGeneric.SUCCESS -> hideLoader()
            }
        }
        dogListViewModel.dogList.observe(this) { downloadDogs(it) }
    }

    private fun showLoader() {
        binding.loadingWheel.visible(true)
    }

    private fun hideLoader() {
        binding.loadingWheel.visible(false)
    }

    private fun downloadDogs(dogList: List<Dog>) {
        dogAdapter.submitList(dogList)
    }

    private fun initList() {
        dogAdapter = DogAdapter() {
            val intent = Intent(this, DogDetailActivity::class.java)
            intent.putExtra(DOG_KEY, it)
            startActivity(intent)
        }
        dogAdapter.setLongOnItemClickListener {
            dogListViewModel.addDogFavorite(it.id)
        }
        binding.dogRecycler.apply {
            layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT)
            adapter = dogAdapter
        }
    }
}