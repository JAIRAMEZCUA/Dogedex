package com.hackaprende.dogedex.ui.dog.dogList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatus
import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatusGeneric
import com.hackaprende.dogedex.data.network.repository.DogRepository
import com.hackaprende.dogedex.data.network.repository.UserAddDogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DogListViewModel : ViewModel() {
    private var dogRepo = DogRepository()
    private var addDogToUser = UserAddDogRepository()

    //    TODO esto se puede simplificar pero lo hice asi para practicar los flows
    private val _statusDownload = MutableStateFlow<ApiResponseStatus>(ApiResponseStatus.LOADING)
    val statusDownload: StateFlow<ApiResponseStatus>
        get() = _statusDownload

    private val _status = MutableLiveData<ApiResponseStatusGeneric<Any>>()
    val status: LiveData<ApiResponseStatusGeneric<Any>>
        get() = _status

    fun addDogFavorite(dogId: Long) {
        viewModelScope.launch {
            handleAddDogToUserResponseStatus(addDogToUser.addDogToUser(dogId))
        }
    }

    private fun downloadUserDogs() {
        viewModelScope.launch {
            handleAddDogToUserResponseStatus(addDogToUser.getDogToUser())
        }
    }

    private fun downloadDogs() {
        viewModelScope.launch {
//            Necesitamos meter StateFlow ya que estan escuchao el canal si cambiamos de estado
//            En cambio en LiveData solo esta escuchando una vez
            _statusDownload.value = dogRepo.downloadDogs()
        }
    }

    private fun handleAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatusGeneric<Any>) {
        _status.value = ApiResponseStatusGeneric.Loading()
        _status.value = apiResponseStatus
    }
}


//TODO mostrar la foto de los perros que tengamos agregados en favrito y los que no solo el nombre

//TODO SI QUEREMOS QUE DESCARGUE OTRO VALOR LO QUE HACEMOS ES DESCARGARLO POR DOS PARTES:
//_dogList Y _dogList y hacer su casteo correspondiente a ANY una ve que se obtenga el valor deseado
/*  @Suppress("UNCHECKED_CAST")
    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<List<Dog>>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            _dogList.value = apiResponseStatus.data!!
        }

        _status.value = apiResponseStatus as ApiResponseStatus<Any>
    }*/