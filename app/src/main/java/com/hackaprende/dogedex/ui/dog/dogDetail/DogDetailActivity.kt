package com.hackaprende.dogedex.ui.dog.dogDetail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hackaprende.dogedex.utils.DOG_KEY
import com.hackaprende.dogedex.R
import com.hackaprende.dogedex.data.network.api.models.Dog
import com.hackaprende.dogedex.databinding.ActivityDogDetailBinding
import com.hackaprende.dogedex.utils.loadImageCoin


class DogDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dog = intent?.extras?.getParcelable<Dog>(DOG_KEY)

        if (dog == null) {
            Toast.makeText(this, R.string.error_showing_dog_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.dogNameText.text = dog.name
        binding.dogIndex.text = getString(R.string.dog_index_format, dog.index)
        binding.lifeExpectancy.text =
            getString(R.string.dog_life_expectancy_format, dog.lifeExpectancy)
        binding.dogImage.loadImageCoin(dog.imageUrl)
        binding.closeButton.setOnClickListener {
            finish()
        }
    }
}