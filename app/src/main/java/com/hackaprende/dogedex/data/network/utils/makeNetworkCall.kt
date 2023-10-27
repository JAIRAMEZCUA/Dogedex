package com.hackaprende.dogedex.data.network.utils

import com.hackaprende.dogedex.R
import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatusGeneric
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

suspend fun <T> makeNetworkCall(
    call: suspend () -> T
): ApiResponseStatusGeneric<T> = withContext(Dispatchers.IO) {
    try {
        ApiResponseStatusGeneric.SUCCESS(call())
    } catch (e: UnknownHostException) {
        ApiResponseStatusGeneric.ERROR(R.string.unknown_host_exception_error)
    } catch (e: Exception) {
        val errorMessage = when (e.message) {
            "sign_up_error" -> R.string.error_sign_up
            "sign_in_error" -> R.string.error_sign_in
            "user_already_exists" -> R.string.user_already_exists
            else -> R.string.unknown_error
        }
        ApiResponseStatusGeneric.ERROR(errorMessage)
    }
}