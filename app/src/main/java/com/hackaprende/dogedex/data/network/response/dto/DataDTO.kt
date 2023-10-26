package com.hackaprende.dogedex.data.network.response.dto

import com.google.gson.annotations.SerializedName


data class DataDTO(
    @SerializedName("dogs") var dogs: ArrayList<DogDTO>
)