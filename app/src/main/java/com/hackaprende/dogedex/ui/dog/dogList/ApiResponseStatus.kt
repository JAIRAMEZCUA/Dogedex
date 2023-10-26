package com.hackaprende.dogedex.ui.dog.dogList

import com.hackaprende.dogedex.data.network.api.models.Dog

sealed class ApiResponseStatus() {
    data class SUCCESS(val dogList: List<Dog>) : ApiResponseStatus()
    data class ERROR(val error: Int) : ApiResponseStatus()
    object LOADING : ApiResponseStatus()
}

