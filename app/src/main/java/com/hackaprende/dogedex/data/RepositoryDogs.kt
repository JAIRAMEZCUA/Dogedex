package com.hackaprende.dogedex.data

import com.hackaprende.dogedex.R
import com.hackaprende.dogedex.data.network.NetworkModule.retrofitService
import com.hackaprende.dogedex.data.network.api.models.Dog
import com.hackaprende.dogedex.data.network.response.dtoMapper.DogDTOMapper
import com.hackaprende.dogedex.ui.dog.dogList.ApiResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.UnknownHostException

class RepositoryDogs {

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


    /*
        private fun getFakeDogs(): MutableList<DogModel> {
            val dogList = mutableListOf<DogModel>()
            dogList.add(
                DogModel(
                    1, 1, "Chihuahua", "Toy", 5.4,
                    6.7, "", "12 - 15", "", 10.5,
                    12.3
                )
            )
            dogList.add(
                DogModel(
                    2, 1, "Labrador", "Toy", 5.4,
                    6.7, "", "12 - 15", "", 10.5,
                    12.3
                )
            )
            dogList.add(
                DogModel(
                    3, 1, "Retriever", "Toy", 5.4,
                    6.7, "", "12 - 15", "", 10.5,
                    12.3
                )
            )
            dogList.add(
                DogModel(
                    4, 1, "San Bernardo", "Toy", 5.4,
                    6.7, "", "12 - 15", "", 10.5,
                    12.3
                )
            )
            dogList.add(
                DogModel(
                    5, 1, "Husky", "Toy", 5.4,
                    6.7, "", "12 - 15", "", 10.5,
                    12.3
                )
            )
            dogList.add(
                DogModel(
                    6, 1, "Xoloscuincle", "Toy", 5.4,
                    6.7, "", "12 - 15", "", 10.5,
                    12.3
                )
            )
            return dogList
        }*/
}