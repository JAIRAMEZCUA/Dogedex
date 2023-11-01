package com.hackaprende.dogedex.data.network.dto.favorite

import com.google.gson.annotations.SerializedName

class DefaultResponse(
    val message: String,
    @SerializedName("is_success") val isSuccess: Boolean,
)