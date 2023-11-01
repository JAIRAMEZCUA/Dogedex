package com.hackaprende.dogedex.data.network.repository

import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatusGeneric
import com.hackaprende.dogedex.data.network.dto.favorite.AddDogToUser
import com.hackaprende.dogedex.data.network.utils.NetworkModule.retrofitService
import com.hackaprende.dogedex.data.network.utils.makeNetworkCall

class UserAddDogRepository {
    suspend fun addDogToUser(dogId: Long): ApiResponseStatusGeneric<Any> {
        return makeNetworkCall {
            val addDogFav = AddDogToUser(dogId)
            val addDogToUserApiResponse = retrofitService.addDogFav(addDogFav)
            val isSuccess = addDogToUserApiResponse.isSuccess
            if (!isSuccess) {
                throw Exception(addDogToUserApiResponse.message)
            }
        }
    }
}