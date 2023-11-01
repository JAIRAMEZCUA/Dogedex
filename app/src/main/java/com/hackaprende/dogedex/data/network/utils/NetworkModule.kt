package com.hackaprende.dogedex.data.network.utils

import com.hackaprende.dogedex.data.network.api.ApiServiceInterceptor
import com.hackaprende.dogedex.data.network.api.DogApiService
import com.hackaprende.dogedex.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private val okHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(ApiServiceInterceptor)
            .build()

    private val retrofit = Retrofit
        .Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val retrofitService: DogApiService by lazy {
        //Lazy es para llamarlo hasta que se implemente
        retrofit.create(DogApiService::class.java)
    }

}