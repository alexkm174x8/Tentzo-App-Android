package com.example.apptentzo_android.ui.model


import retrofit2.http.GET
import retrofit2.http.Path

interface MeteomaticsApiService {
    @GET("{startDate}--{endDate}:{timeStep}/t_2m:C,weather_code:code/{latitude},{longitude}/json")
    suspend fun getWeatherData(
        @Path("startDate") startDate: String, // e.g., "2024-11-10T20:40:00.000-06:00"
        @Path("endDate") endDate: String,     // e.g., "2024-11-30T20:40:00.000-06:00"
        @Path("timeStep") timeStep: String,   // e.g., "PT10M"
        @Path("latitude") latitude: Double,   // e.g., 18.9296223
        @Path("longitude") longitude: Double  // e.g., -98.292414

    ): MeteoResponse
}
