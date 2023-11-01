package com.hackaprende.dogedex.data.network.repository

import com.hackaprende.dogedex.data.network.api.models.Dog
import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatusGeneric
import com.hackaprende.dogedex.data.network.dto.dtoMapper.DogDTOMapper
import com.hackaprende.dogedex.data.network.utils.NetworkModule.retrofitService
import com.hackaprende.dogedex.data.network.utils.makeNetworkCall

class DogRepository {
    suspend fun downloadDogs(): ApiResponseStatusGeneric<List<Dog>> {
        return makeNetworkCall {
            val dogListApiResponse = retrofitService.getListDogs()
            val dogDTOList = dogListApiResponse.data.dogs
            DogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
        }
    }
}