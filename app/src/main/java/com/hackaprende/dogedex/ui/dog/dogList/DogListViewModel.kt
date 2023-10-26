package com.hackaprende.dogedex.ui.dog.dogList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaprende.dogedex.data.RepositoryDogs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DogListViewModel : ViewModel() {
    private var dogRepo = RepositoryDogs()

    private val _statusDownload = MutableStateFlow<ApiResponseStatus>(ApiResponseStatus.LOADING)
    val statusDownload: StateFlow<ApiResponseStatus>
        get() = _statusDownload

    init {
        downloadDogs()
    }

    private fun downloadDogs() {
        viewModelScope.launch {
//            Necesitamos meter StateFlow ya que estan escuchao el canal si cambiamos de estado
//            En cambio en LiveData solo esta escuchando una vez
            _statusDownload.value = dogRepo.downloadDogs()
        }
    }
}