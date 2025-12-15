package com.example.levelup.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BackendClient {
    // 10.0.2.2 es la dirección especial para acceder al localhost de la máquina de desarrollo desde el emulador Android
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: BackendApiService by lazy {
        retrofit.create(BackendApiService::class.java)
    }
}