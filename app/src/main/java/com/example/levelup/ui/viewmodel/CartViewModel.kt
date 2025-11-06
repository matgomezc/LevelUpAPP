package com.example.levelup.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.model.CartItem
import com.example.levelup.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Estado para el carrito
data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val showSuccessMessage: Boolean = false
) {
    val totalItems: Int
        get() = items.sumOf { it.quantity }
    
    val totalPrice: Double
        get() = items.sumOf { it.totalPrice }
}

// ViewModel para manejar el carrito
class CartViewModel(application: Application) : AndroidViewModel(application) {
    
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()
    
    // Agregar producto al carrito
    fun addToCart(product: Product) {
        val currentItems = _uiState.value.items.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.product.id == product.id }
        
        if (existingItemIndex >= 0) {
            // Si el producto ya está en el carrito, aumentar la cantidad
            val existingItem = currentItems[existingItemIndex]
            currentItems[existingItemIndex] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            // Si no está, agregarlo
            currentItems.add(CartItem(product = product, quantity = 1))
        }
        
        _uiState.value = _uiState.value.copy(items = currentItems)
    }
    
    // Eliminar producto del carrito
    fun removeFromCart(productId: Long) {
        val currentItems = _uiState.value.items.toMutableList()
        currentItems.removeAll { it.product.id == productId }
        _uiState.value = _uiState.value.copy(items = currentItems)
    }
    
    // Actualizar cantidad de un producto
    fun updateQuantity(productId: Long, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
            return
        }
        
        val currentItems = _uiState.value.items.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.product.id == productId }
        
        if (existingItemIndex >= 0) {
            val existingItem = currentItems[existingItemIndex]
            currentItems[existingItemIndex] = existingItem.copy(quantity = quantity)
            _uiState.value = _uiState.value.copy(items = currentItems)
        }
    }
    
    // Vaciar el carrito
    fun clearCart() {
        _uiState.value = _uiState.value.copy(items = emptyList())
    }
    
    // Comprar (vaciar carrito y mostrar mensaje de éxito)
    fun purchase() {
        _uiState.value = _uiState.value.copy(
            showSuccessMessage = true,
            items = emptyList()
        )
    }
    
    // Ocultar mensaje de éxito
    fun hideSuccessMessage() {
        _uiState.value = _uiState.value.copy(showSuccessMessage = false)
    }
}

