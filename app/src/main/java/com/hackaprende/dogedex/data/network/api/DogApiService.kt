package com.hackaprende.dogedex.data.network.api

import com.hackaprende.dogedex.data.network.dto.dog.DataDogResponse
import com.hackaprende.dogedex.data.network.dto.favorite.AddDogToUser
import com.hackaprende.dogedex.data.network.dto.favorite.DefaultResponse
import com.hackaprende.dogedex.data.network.dto.signIn.SignInDTO
import com.hackaprende.dogedex.data.network.dto.signUp.SignAuthApiResponse
import com.hackaprende.dogedex.data.network.dto.signUp.SignUpDTO
import com.hackaprende.dogedex.utils.ADD_DOG_FAV
import com.hackaprende.dogedex.utils.GET_ALL_DOGS
import com.hackaprende.dogedex.utils.GET_DOGS_USER
import com.hackaprende.dogedex.utils.NEEDS_AUTH_HEADER_KEY
import com.hackaprende.dogedex.utils.SIGN_IN_URL
import com.hackaprende.dogedex.utils.SIGN_UP_URL
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface DogApiService {
    @GET(GET_ALL_DOGS)
    suspend fun getListDogs(): DataDogResponse

    @POST(SIGN_UP_URL)
    suspend fun signUp(@Body signUpDTO: SignUpDTO): SignAuthApiResponse

    @POST(SIGN_IN_URL)
    suspend fun signIn(@Body signInDTO: SignInDTO): SignAuthApiResponse

    @Headers("$NEEDS_AUTH_HEADER_KEY: true")
    @POST(ADD_DOG_FAV)
    suspend fun addDogFav(@Body addDogFav: AddDogToUser): DefaultResponse

    @Headers("$NEEDS_AUTH_HEADER_KEY: true")
    @GET(GET_DOGS_USER)
    suspend fun getDogFavorite(): DataDogResponse

}