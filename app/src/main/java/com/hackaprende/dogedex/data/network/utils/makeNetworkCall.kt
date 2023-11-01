package com.hackaprende.dogedex.data.network.utils

import com.hackaprende.dogedex.R
import com.hackaprende.dogedex.data.network.api.sealed.ApiResponseStatusGeneric
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException


private const val UNAUTHORIZED_ERROR_CODE = 401

suspend fun <T> makeNetworkCall(
    call: suspend () -> T
): ApiResponseStatusGeneric<T> = withContext(Dispatchers.IO) {
    try {
        ApiResponseStatusGeneric.SUCCESS(call())
    } catch (e: UnknownHostException) {
        ApiResponseStatusGeneric.ERROR(R.string.unknown_host_exception_error)
    } catch (e: HttpException) {
        val errorMessage = if (e.code() == UNAUTHORIZED_ERROR_CODE) {
            R.string.wrong_user_or_password
        } else {
            R.string.unknown_error
        }
        ApiResponseStatusGeneric.ERROR(errorMessage)
    } catch (e: Exception) {
        val errorMessage = when (e.message) {
            "sign_up_error" -> R.string.error_sign_up
            "sign_in_error" -> R.string.error_sign_in
            "user_already_exists" -> R.string.user_already_exists
            "error_adding_dog" -> R.string.error_adding_dog
            else -> R.string.unknown_error
        }
        ApiResponseStatusGeneric.ERROR(errorMessage)
    }
}