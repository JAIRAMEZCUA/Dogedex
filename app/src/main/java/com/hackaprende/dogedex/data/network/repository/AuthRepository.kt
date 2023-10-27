package com.hackaprende.dogedex.data.network.repository

import com.hackaprende.dogedex.data.network.NetworkModule.retrofitService
import com.hackaprende.dogedex.data.network.api.models.User
import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatusGeneric
import com.hackaprende.dogedex.data.network.dto.SignUpDTO
import com.hackaprende.dogedex.data.network.dto.dtoMapper.UserDTOMapper
import com.hackaprende.dogedex.data.network.utils.makeNetworkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    suspend fun signInUser(
        email: String,
        password: String,
        passwordConfirmation: String
    ): ApiResponseStatusGeneric<User> {
        return makeNetworkCall {
            val userSignUp = SignUpDTO(email, password, passwordConfirmation)
            val signUpUser = retrofitService.signUp(userSignUp)
            if (!signUpUser.isSuccess) {
                throw Exception(signUpUser.message)
            }
            val userDTO = signUpUser.data.user
            UserDTOMapper.fromUserDTOToUserDomain(userDTO)
        }
    }
}