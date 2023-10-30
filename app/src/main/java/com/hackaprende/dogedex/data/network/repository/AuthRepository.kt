package com.hackaprende.dogedex.data.network.repository

import com.hackaprende.dogedex.data.network.api.models.User
import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatusGeneric
import com.hackaprende.dogedex.data.network.dto.dtoMapper.UserDTOMapper
import com.hackaprende.dogedex.data.network.dto.signIn.SignInDTO
import com.hackaprende.dogedex.data.network.dto.signUp.SignUpDTO
import com.hackaprende.dogedex.data.network.utils.NetworkModule.retrofitService
import com.hackaprende.dogedex.data.network.utils.makeNetworkCall

class AuthRepository {
    suspend fun signUpUser(
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


    suspend fun signInUser(
        email: String,
        password: String
    ): ApiResponseStatusGeneric<User> {
        return makeNetworkCall {
            val signInDTO = SignInDTO(email, password)
            val signInUser = retrofitService.signIn(signInDTO)
            if (!signInUser.isSuccess) {
                throw Exception(signInUser.message)
            }
            val userDTO = signInUser.data.user
            UserDTOMapper.fromUserDTOToUserDomain(userDTO)
        }
    }

}