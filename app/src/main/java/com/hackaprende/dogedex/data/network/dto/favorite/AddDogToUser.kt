package com.hackaprende.dogedex.data.network.dto.favorite

import com.google.gson.annotations.SerializedName

data class AddDogToUser(
    @SerializedName("dog_id") var dogId: Long,
)