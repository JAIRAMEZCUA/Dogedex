package com.hackaprende.dogedex.data.network.dto

import com.google.gson.annotations.SerializedName


data class DataResponse(
    @SerializedName("message") var message: String? = null,
    @SerializedName("is_success") var isSuccess: Boolean,
    @SerializedName("data") var data: DataDTO
)