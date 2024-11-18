package com.example.apptentzo_android.ui.model

data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val foto_perfil: String? = null,
    val plantas: Int = 0,
    val rutas: Int = 0,
    val user_id: String = "",
    val email: String = ""
)