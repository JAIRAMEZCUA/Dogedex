package com.hackaprende.dogedex.data.network.dto

import com.google.gson.annotations.SerializedName

class SignUpApiResponse(
    val message: String,
    @SerializedName("is_success") val isSuccess: Boolean,
    val data: UserResponse,
)