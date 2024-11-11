package com.example.apptentzo_android.ui.model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MeteoModels {
    private const val BASE_URL = "https://api.meteomatics.com/"

    fun getRetrofit(username: String, password: String): Retrofit {
        // Crear el interceptor de logging
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor(username, password))
            .addInterceptor(loggingInterceptor) // Añadir el interceptor de logging
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
