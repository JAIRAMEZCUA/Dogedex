package com.hackaprende.dogedex.data.network.api

import com.hackaprende.dogedex.GET_ALL_DOGS
import com.hackaprende.dogedex.data.network.response.dto.DataResponse
import retrofit2.http.GET

interface DogApiService {
    @GET(GET_ALL_DOGS)
    suspend fun getListDogs(): DataResponse
}