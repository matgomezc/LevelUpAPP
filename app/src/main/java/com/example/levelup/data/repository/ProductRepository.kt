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
    
    // Obtener un producto por ID
    suspend fun getProductById(productId: Long): Product? = productDao.getProductById(productId)
    
    // Agregar un nuevo producto
    suspend fun insertProduct(product: Product): Long = productDao.insertProduct(product)
    
    // Actualizar un producto
    suspend fun updateProduct(product: Product) = productDao.updateProduct(product)
    
    // Eliminar un producto
    suspend fun deleteProduct(productId: Long) = productDao.deleteProduct(productId)
    
    // Obtener todas las categorías
    suspend fun getAllCategories(): List<String> = productDao.getAllCategories()
}

