package com.hackaprende.dogedex.data.network.repository

import com.hackaprende.dogedex.R
import com.hackaprende.dogedex.data.network.utils.NetworkModule.retrofitService
import com.hackaprende.dogedex.data.network.dto.dtoMapper.DogDTOMapper
import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class DogRepository {
    suspend fun downloadDogs(): ApiResponseStatus {
        return withContext(Dispatchers.IO) {
            try {
                ApiResponseStatus.LOADING
                val dogListApiResponse = retrofitService.getListDogs()
                val dogDTOList = dogListApiResponse.data.dogs
                ApiResponseStatus.SUCCESS(DogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList))
            } catch (e: UnknownHostException) {
                ApiResponseStatus.ERROR(R.string.unknown_host_exception_error)
            } catch (e: Exception) {
                ApiResponseStatus.ERROR(R.string.unknown_error)
            }
        }
    }
}