package com.example.apptentzo_android.ui.model

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor(private val username: String, private val password: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val credentials = Credentials.basic(username, password)
        val request = chain.request().newBuilder()
            .header("Authorization", credentials)
            .build()
        return chain.proceed(request)
    }
}
