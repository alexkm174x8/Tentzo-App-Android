package com.example.apptentzo_android.ui.model

data class MeteoResponse(
    val geometry: Geometry,
    val properties: Properties
)

data class Geometry(
    val type: String,
    val coordinates: List<List<Double>>
)

data class Properties(
    val timeseries: List<TimeSeries>
)

data class TimeSeries(
    val validTime: String,
    val parameters: List<Parameter>
)

data class Parameter(
    val name: String,
    val unit: String,
    val values: List<Double>
)
