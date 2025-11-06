package com.example.levelup.data.repository

import com.example.levelup.data.dao.ProductDao
import com.example.levelup.data.model.Product

// Repositorio para manejar productos
class ProductRepository(private val productDao: ProductDao) {
    
    // Obtener todos los productos
    suspend fun getAllProducts(): List<Product> = productDao.getAllProducts()
    
    // Obtener productos por categoría
    suspend fun getProductsByCategory(category: String): List<Product> = 
        productDao.getProductsByCategory(category)
    
    // Agregar un nuevo producto
    suspend fun insertProduct(product: Product): Long = productDao.insertProduct(product)
}

