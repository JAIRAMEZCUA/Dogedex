package com.hackaprende.dogedex.data.network.repository

import com.hackaprende.dogedex.R
import com.hackaprende.dogedex.data.network.api.models.Dog
import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatusGeneric
import com.hackaprende.dogedex.data.network.dto.dtoMapper.DogDTOMapper
import com.hackaprende.dogedex.data.network.dto.favorite.AddDogToUser
import com.hackaprende.dogedex.data.network.utils.NetworkModule.retrofitService
import com.hackaprende.dogedex.data.network.utils.makeNetworkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class DogRepository {

    suspend fun getDogCollection(): ApiResponseStatusGeneric<List<Dog>> {
        return withContext(Dispatchers.IO) {
            //Esto es deferred que lo que hace so peticiones asyncronas , y las dos se descargan simultaneamente
            val allDogsListDeferred = async { downloadDogs() }
            val userDogsListDeferred = async { getUserDogs() }

            //response , espera a que todos los procesos terminen de descargar
            val allDogsListResponse = allDogsListDeferred.await()
            val userDogsListResponse = userDogsListDeferred.await()

            if (allDogsListResponse is ApiResponseStatusGeneric.ERROR) {
                allDogsListResponse
            } else if (userDogsListResponse is ApiResponseStatusGeneric.ERROR) {
                userDogsListResponse
            } else if (allDogsListResponse is ApiResponseStatusGeneric.SUCCESS &&
                userDogsListResponse is ApiResponseStatusGeneric.SUCCESS
            ) {
                ApiResponseStatusGeneric.SUCCESS(
                    getCollectionList(
                        allDogsListResponse.data,
                        userDogsListResponse.data
                    )
                )
            } else {
                ApiResponseStatusGeneric.ERROR(R.string.unknown_error)
            }
        }
    }

    private fun getCollectionList(allDogList: List<Dog>, userDogList: List<Dog>) =
        allDogList.map {
            if (userDogList.contains(it)) {
                it
            } else {
                Dog(
                    it.id, it.index, "", "", "", "",
                    "", "", "", "", "", inCollection = false
                )
            }
        }
            .sorted() //ordena la lista de acuerdo al comparable en dog , que se ordena de acuedo al index

    private suspend fun downloadDogs(): ApiResponseStatusGeneric<List<Dog>> = makeNetworkCall {
        val dogListApiResponse = retrofitService.getListDogs()
        val dogDTOList = dogListApiResponse.data.dogs
        DogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
    }

    private suspend fun getUserDogs(): ApiResponseStatusGeneric<List<Dog>> = makeNetworkCall {
        val dogListApiResponse = retrofitService.getDogFavorite()
        val dogDTOList = dogListApiResponse.data.dogs
        DogDTOMapper.fromDogDTOListToDogDomainList(dogDTOList)
    }

    suspend fun addDogToUser(dogId: Long): ApiResponseStatusGeneric<Any> = makeNetworkCall {
        val addDogToUserDTO = AddDogToUser(dogId)
        val defaultResponse = retrofitService.addDogFav(addDogToUserDTO)
        if (!defaultResponse.isSuccess) {
            throw Exception(defaultResponse.message)
        }
    }
}


