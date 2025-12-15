package com.example.levelup.data.network.model

import com.google.gson.annotations.SerializedName

// Lo que enviamos para Login
data class LoginRequest(
    val email: String,
    val password: String
)

// Lo que enviamos para Registro
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

// Lo que responde el servidor (asumiendo JWT y datos de usuario)
data class AuthResponse(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: Long,
    val name: String,
    val email: String,
    @SerializedName("avatar_url") val avatarUrl: String? = null
)