package com.example.levelup.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.levelup.data.model.Product

@Dao
interface ProductDao {
    @Insert
    suspend fun insertProduct(product: Product): Long
    
    @Query("SELECT * FROM products ORDER BY name ASC")
    suspend fun getAllProducts(): List<Product>
    
    @Query("SELECT * FROM products WHERE LOWER(category) = LOWER(:category) ORDER BY name ASC")
    suspend fun getProductsByCategory(category: String): List<Product>
}

