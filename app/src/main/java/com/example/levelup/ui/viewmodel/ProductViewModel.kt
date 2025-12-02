package com.example.levelup.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.levelup.data.database.LevelUpDatabase
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
class ProductViewModel(
    application: Application,
    private val productRepository: ProductRepository
) : AndroidViewModel(application) {
    
    // Estado actual
    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()
    
    init {
        // Inicializar productos de ejemplo si la base de datos está vacía
        ProductSeeder.seedProducts(productRepository, viewModelScope)
        
        // Cargar productos al iniciar
        loadProducts()
    }
    
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                val database = LevelUpDatabase.getDatabase(application)
                val repository = ProductRepository(database.productDao())
                ProductViewModel(application, repository)
            }
        }
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
    
    // Sincronizar con API externa
    fun syncWithApi() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                productRepository.syncProducts()
                // Recargar desde local para ver los cambios
                val products = productRepository.getAllProducts()
                _uiState.value = _uiState.value.copy(
                    products = products,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al sincronizar con API: ${e.message}"
                )
            }
        }
    }
    
}