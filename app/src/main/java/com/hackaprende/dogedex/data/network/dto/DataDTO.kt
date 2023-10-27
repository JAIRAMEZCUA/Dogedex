package com.hackaprende.dogedex.data.network.dto

import com.google.gson.annotations.SerializedName


data class DataDTO(
    @SerializedName("dogs") var dogs: ArrayList<DogDTO>
)