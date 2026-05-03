package com.example.sicenetmultiplatform.data.model

data class LoginResult(
    val success: Boolean,
    val cookie: String? = null,
    val message: String? = null,
    val sinConexion: Boolean = false
)
