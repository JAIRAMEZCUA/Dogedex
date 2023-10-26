package com.hackaprende.dogedex.data.network

import com.hackaprende.dogedex.BASE_URL
import com.hackaprende.dogedex.data.network.api.DogApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object NetworkModule {


    private val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val retrofitService: DogApiService by lazy {
        //Lazy es para llamarlo hasta que se implemente
        retrofit.create(DogApiService::class.java)
    }

}