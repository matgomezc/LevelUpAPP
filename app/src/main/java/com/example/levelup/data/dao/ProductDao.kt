package com.example.levelup.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.levelup.data.model.Product

@Dao
interface ProductDao {
    @Insert
    suspend fun insertProduct(product: Product): Long
    
    @Update
    suspend fun updateProduct(product: Product)
    
    @Query("SELECT * FROM products ORDER BY name ASC")
    suspend fun getAllProducts(): List<Product>
    
    @Query("SELECT * FROM products WHERE category = :category ORDER BY name ASC")
    suspend fun getProductsByCategory(category: String): List<Product>
    
    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    suspend fun getProductById(productId: Long): Product?
    
    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteProduct(productId: Long)
    
    @Query("SELECT DISTINCT category FROM products ORDER BY category ASC")
    suspend fun getAllCategories(): List<String>
}

