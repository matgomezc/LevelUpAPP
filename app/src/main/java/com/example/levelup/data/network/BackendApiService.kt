package com.example.levelup.data.network

import com.example.levelup.data.network.model.AuthResponse
import com.example.levelup.data.network.model.LoginRequest
import com.example.levelup.data.network.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface BackendApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}