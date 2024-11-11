package com.example.apptentzo_android.ui.model

import com.example.apptentzo_android.BuildConfig

class WeatherRepository {
    private val api: MeteomaticsApiService

    init {
        val retrofit = MeteoModels.getRetrofit(
            username = BuildConfig.METEOMATICS_USERNAME,
            password = BuildConfig.METEOMATICS_PASSWORD
        )
        api = retrofit.create(MeteomaticsApiService::class.java)
    }

    suspend fun getWeatherData(
        startDate: String,
        endDate: String,
        timeStep: String,
        latitude: Double,
        longitude: Double
    ): MeteoResponse {
        return api.getWeatherData(startDate, endDate, timeStep, latitude, longitude)
    }
}
