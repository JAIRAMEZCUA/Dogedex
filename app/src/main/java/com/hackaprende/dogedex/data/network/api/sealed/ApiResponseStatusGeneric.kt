package com.hackaprende.dogedex.data.network.api.sealed

sealed class ApiResponseStatusGeneric<T> {
    class SUCCESS<T>(val data: T) : ApiResponseStatusGeneric<T>()
    class ERROR<T>(val error: Int) : ApiResponseStatusGeneric<T>()
    class Loading<T> : ApiResponseStatusGeneric<T>()
}
