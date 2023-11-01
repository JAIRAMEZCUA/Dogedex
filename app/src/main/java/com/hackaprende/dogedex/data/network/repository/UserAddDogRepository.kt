package com.hackaprende.dogedex.data.network.repository

import com.hackaprende.dogedex.R
import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatus
import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatusGeneric
import com.hackaprende.dogedex.data.network.dto.dtoMapper.DogDTOMapper
import com.hackaprende.dogedex.data.network.dto.favorite.AddDogToUser
import com.hackaprende.dogedex.data.network.utils.NetworkModule.retrofitService
import com.hackaprende.dogedex.data.network.utils.makeNetworkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

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

    suspend fun getDogToUser(): ApiResponseStatusGeneric<Any> {
        return makeNetworkCall {
            val getDogFavorite = retrofitService.getDogFavorite()
            val isSuccess = getDogFavorite.isSuccess
            if (!isSuccess) {
                throw Exception(getDogFavorite.message)
            }
            DogDTOMapper.fromDogDTOListToDogDomainList(getDogFavorite.data.dogs)
        }
    }

    //TODO CON LOS FLOWS ASI SE VERIA LA PETICION DEBIDO A QUE ESTARIAMOS MANDANDO DESDE AQUI LOS ESTADOS(LOAD, SUCCESS, ERROR)
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

