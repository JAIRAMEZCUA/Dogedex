package com.hackaprende.dogedex.data.network.dto.dog

import com.google.gson.annotations.SerializedName


data class DataDogResponse(
    @SerializedName("message") var message: String? = null,
    @SerializedName("is_success") var isSuccess: Boolean,
    @SerializedName("data") var data: DataDTO
)