package com.hackaprende.dogedex.data.network.api

import com.hackaprende.dogedex.data.network.dto.DataResponse
import com.hackaprende.dogedex.data.network.dto.SignUpApiResponse
import com.hackaprende.dogedex.data.network.dto.SignUpDTO
import com.hackaprende.dogedex.utils.GET_ALL_DOGS
import com.hackaprende.dogedex.utils.SIGN_UP_URL
import retrofit2.http.Body
import retrofit2.http.GET

interface DogApiService {
    @GET(GET_ALL_DOGS)
    suspend fun getListDogs(): DataResponse

    @GET(SIGN_UP_URL)
    suspend fun signUp(@Body signUpDTO: SignUpDTO): SignUpApiResponse

}