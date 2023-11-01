package com.hackaprende.dogedex.ui.user.auth.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaprende.dogedex.data.network.api.models.User
import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatusGeneric
import com.hackaprende.dogedex.data.network.repository.AuthRepository
import kotlinx.coroutines.launch


class LoginActivityViewModel() : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _status = MutableLiveData<ApiResponseStatusGeneric<User>>()
    val status: LiveData<ApiResponseStatusGeneric<User>>
        get() = _status


    private var authRepository = AuthRepository()


    fun signUp(
        email: String, password: String,
        passwordConfirmation: String
    ) {
        viewModelScope.launch {
            _status.value = ApiResponseStatusGeneric.LOADING()
            handleResponseStatus(
                authRepository.signUpUser(
                    email,
                    password, passwordConfirmation
                )
            )
        }
    }


    fun signIn(
        email: String, password: String
    ) {
        viewModelScope.launch {
            _status.value = ApiResponseStatusGeneric.LOADING()
            handleResponseStatus(
                authRepository.signInUser(
                    email,
                    password
                )
            )
        }
    }

    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatusGeneric<User>) {
        if (apiResponseStatus is ApiResponseStatusGeneric.SUCCESS) {
            _user.value = apiResponseStatus.data!!
        }
        _status.value = apiResponseStatus
    }
}