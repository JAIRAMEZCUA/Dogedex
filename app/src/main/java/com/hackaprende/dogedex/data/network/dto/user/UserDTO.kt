package com.hackaprende.dogedex.data.network.dto.user

import com.google.gson.annotations.SerializedName

class UserDTO(
    val id: Long,
    val email: String,
    @SerializedName("authentication_token") val authenticationToken: String,
)