package com.hackaprende.dogedex.data.network.api.sealed

import com.hackaprende.dogedex.data.network.api.models.Dog

sealed class ApiResponseStatus() {
    data class SUCCESS(val dogList: List<Dog>) : ApiResponseStatus()
    data class ERROR(val error: Int) : ApiResponseStatus()
    data object LOADING : ApiResponseStatus()
}

//Data obect porque no tiene parametros

