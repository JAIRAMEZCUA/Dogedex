package com.hackaprende.dogedex.data.network.api

import com.hackaprende.dogedex.utils.AUTH_TOKEN
import com.hackaprende.dogedex.utils.NEEDS_AUTH_HEADER_KEY
import okhttp3.Interceptor
import okhttp3.Response

object ApiServiceInterceptor : Interceptor {
    private var sessionToken: String? = null

    fun setSessionToken(sessionToken: String) {
        this.sessionToken = sessionToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        if (request.header(NEEDS_AUTH_HEADER_KEY) != null) {
            // needs credentials
            if (sessionToken == null) {
                throw RuntimeException("Need to be authenticated to perform this request")
            } else {
                requestBuilder.addHeader(AUTH_TOKEN, sessionToken!!)
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}