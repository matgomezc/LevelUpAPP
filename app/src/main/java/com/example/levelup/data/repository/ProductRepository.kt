package com.example.levelup.data.repository

import com.example.levelup.data.dao.ProductDao
import com.example.levelup.data.model.Product
import com.example.levelup.data.network.RetrofitClient
import com.example.levelup.data.network.model.toProductList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repositorio para manejar productos
class ProductRepository(private val productDao: ProductDao) {
    
    private val apiService = RetrofitClient.apiService
    
    // Obtener todos los productos (Local)
    suspend fun getAllProducts(): List<Product> = productDao.getAllProducts()
    
    // Obtener productos de la API externa
    suspend fun getProductsFromApi(): List<Product> {
        return try {
            val apiProducts = apiService.getProducts()
            apiProducts.toProductList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Sincronizar productos de la API a la base de datos local
    suspend fun syncProducts() {
        withContext(Dispatchers.IO) {
            try {
                val apiProducts = apiService.getProducts()
                val products = apiProducts.toProductList()
                
                // Insertar productos si no existen (o actualizar)
                // Nota: Esto es una simplificación. En un caso real habría que manejar conflictos de IDs
                products.forEach { product ->
                    // Verificamos si ya existe para no sobrescribir datos locales si no queremos
                    // O simplemente insertamos todo
                    // Como los IDs pueden chocar, podríamos ignorar el ID de la API y dejar que Room genere uno,
                    // o usar el ID de la API si queremos consistencia.
                    
                    // Para este ejemplo, insertaremos como nuevos si no existen
                    // Pero como productDao.insertProduct devuelve Long, asumimos inserción simple
                    
                    // Una estrategia mejor es verificar si la DB está vacía
                    if (productDao.getAllProducts().isEmpty()) {
                         productDao.insertProduct(product)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    // Obtener productos por categoría
    suspend fun getProductsByCategory(category: String): List<Product> = 
        productDao.getProductsByCategory(category)
    
    // Agregar un nuevo producto
    suspend fun insertProduct(product: Product): Long = productDao.insertProduct(product)
}