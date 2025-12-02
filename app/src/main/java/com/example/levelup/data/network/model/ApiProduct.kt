package com.example.levelup.data.network.model

import com.google.gson.annotations.SerializedName

data class ApiProduct(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: ApiRating
)

data class ApiRating(
    val rate: Double,
    val count: Int
)