package com.hackaprende.dogedex.data.network.dto.dog

import com.google.gson.annotations.SerializedName
import com.hackaprende.dogedex.data.network.dto.dog.DogDTO


data class DataDTO(
    @SerializedName("dogs") var dogs: ArrayList<DogDTO>
)