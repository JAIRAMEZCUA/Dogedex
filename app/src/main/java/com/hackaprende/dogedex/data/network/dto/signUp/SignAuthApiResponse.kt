package com.hackaprende.dogedex.data.network.dto.signUp

import com.google.gson.annotations.SerializedName
import com.hackaprende.dogedex.data.network.dto.user.UserResponse

class SignAuthApiResponse(
    val message: String,
    @SerializedName("is_success") val isSuccess: Boolean,
    val data: UserResponse,
)