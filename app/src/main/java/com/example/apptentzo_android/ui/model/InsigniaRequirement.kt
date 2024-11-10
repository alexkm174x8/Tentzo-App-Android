package com.example.apptentzo_android.ui.model

data class InsigniaRequirement(
    val insigniaId: String,
    val type: String, // "rutas" or "plantas"
    val requiredCount: Int
)