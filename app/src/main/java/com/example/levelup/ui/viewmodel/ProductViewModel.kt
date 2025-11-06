package com.example.levelup.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.model.Product
import com.example.levelup.data.repository.ProductRepository
import com.example.levelup.data.seed.ProductSeeder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estado para los productos
data class ProductUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// ViewModel para manejar los productos
class ProductViewModel(application: Application) : AndroidViewModel(application) {
    
    private val productRepository: ProductRepository
    
    // Estado actual
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()
    
    init {
        // Inicializar repositorio
        val database = com.example.levelup.data.database.LevelUpDatabase.getDatabase(application)
        productRepository = ProductRepository(database.productDao())
        
        // Inicializar productos de ejemplo si la base de datos está vacía
        ProductSeeder.seedProducts(productRepository, viewModelScope)
        
        // Cargar productos al iniciar
        loadProducts()
    }
    
    // Cargar todos los productos
    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val products = productRepository.getAllProducts()
                _uiState.value = _uiState.value.copy(
                    products = products,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar productos: ${e.message}"
                )
            }
        }
    }
    
    // Cargar productos por categoría
    fun loadProductsByCategory(category: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val products = productRepository.getProductsByCategory(category)
                _uiState.value = _uiState.value.copy(
                    products = products,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar productos: ${e.message}"
                )
            }
        }
    }
    
}

