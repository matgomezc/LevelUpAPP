package com.example.levelup.data.network

import com.example.levelup.data.network.model.ApiProduct
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<ApiProduct>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ApiProduct
    
    @GET("products/categories")
    suspend fun getCategories(): List<String>
    
    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<ApiProduct>
}