package com.hackaprende.dogedex.data.network.dto

import com.google.gson.annotations.SerializedName

data class DogDTO(

    @SerializedName("id") var id: Long,
    @SerializedName("dog_type") var type: String,
    @SerializedName("height_female") var heightFemale: String,
    @SerializedName("height_male") var heightMale: String,
    @SerializedName("image_url") var imageUrl: String,
    @SerializedName("index") var index: Int,
    @SerializedName("life_expectancy") var lifeExpectancy: String,
    @SerializedName("name_en") var nameEn: String,
    @SerializedName("name_es") var name: String,
    @SerializedName("temperament") var temperament: String,
    @SerializedName("temperament_en") var temperamentEn: String,
    @SerializedName("weight_female") var weightFemale: String,
    @SerializedName("weight_male") var weightMale: String,
    @SerializedName("created_at") var createdAt: String,
    @SerializedName("updated_at") var updatedAt: String,
    @SerializedName("ml_id") var mlId: String

)