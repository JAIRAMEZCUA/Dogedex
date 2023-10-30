package com.hackaprende.dogedex.data.network.dto.signUp

import com.google.gson.annotations.SerializedName

class SignUpDTO(
    val email: String,
    val password: String,
    @SerializedName("password_confirmation") val passwordConfirmation: String
)