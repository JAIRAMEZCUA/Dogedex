package com.hackaprende.dogedex.ui.dog.dogList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaprende.dogedex.data.network.api.models.Dog
import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatusGeneric
import com.hackaprende.dogedex.data.network.repository.DogRepository
import kotlinx.coroutines.launch

class DogListViewModel : ViewModel() {

    //repos
    private var dogRepo = DogRepository()

    //status respose
    private val _status = MutableLiveData<ApiResponseStatusGeneric<Any>>()
    val status: LiveData<ApiResponseStatusGeneric<Any>>
        get() = _status

    //value response
    private val _dogList = MutableLiveData<List<Dog>>()
    val dogList: LiveData<List<Dog>>
        get() = _dogList

    init {
        downloadDogs()
    }

    fun addDogFavorite(dogId: Long) {
        viewModelScope.launch {
            handleAddDogToUserResponseStatus(dogRepo.addDogToUser(dogId))
        }
    }

    private fun downloadDogs() {
        viewModelScope.launch {
            handleResponseStatus(dogRepo.getDogCollection())
        }
    }

    private fun handleAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatusGeneric<Any>) {
        _status.value = ApiResponseStatusGeneric.LOADING()
        _status.value = apiResponseStatus
    }

    @Suppress("UNCHECKED_CAST")
    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatusGeneric<List<Dog>>) {
        if (apiResponseStatus is ApiResponseStatusGeneric.SUCCESS) {
            _dogList.value = apiResponseStatus.data!!
        }
        _status.value = apiResponseStatus as ApiResponseStatusGeneric<Any>
    }
}