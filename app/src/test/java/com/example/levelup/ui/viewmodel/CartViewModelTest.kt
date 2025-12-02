package com.example.levelup.ui.viewmodel

import android.app.Application
import com.example.levelup.data.model.Product
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CartViewModelTest {

    private lateinit var viewModel: CartViewModel
    private lateinit var application: Application

    @Before
    fun setup() {
        application = mockk(relaxed = true)
        viewModel = CartViewModel(application)
    }

    @Test
    fun `addToCart adds new item correctly`() {
        // Given
        val product = Product(id = 1, name = "PS5", price = 500000.0, category = "Consolas")
        
        // When
        viewModel.addToCart(product)
        
        // Then
        val state = viewModel.uiState.value
        assertEquals(1, state.items.size)
        assertEquals(product, state.items[0].product)
        assertEquals(1, state.items[0].quantity)
    }

    @Test
    fun `addToCart increments quantity for existing item`() {
        // Given
        val product = Product(id = 1, name = "PS5", price = 500000.0, category = "Consolas")
        viewModel.addToCart(product)
        
        // When
        viewModel.addToCart(product)
        
        // Then
        val state = viewModel.uiState.value
        assertEquals(1, state.items.size)
        assertEquals(2, state.items[0].quantity)
        assertEquals(1000000.0, state.items[0].totalPrice, 0.0)
    }

    @Test
    fun `removeFromCart removes item`() {
        // Given
        val product = Product(id = 1, name = "PS5", price = 500000.0, category = "Consolas")
        viewModel.addToCart(product)
        
        // When
        viewModel.removeFromCart(1)
        
        // Then
        val state = viewModel.uiState.value
        assertEquals(0, state.items.size)
    }

    @Test
    fun `updateQuantity updates quantity correctly`() {
        // Given
        val product = Product(id = 1, name = "PS5", price = 500000.0, category = "Consolas")
        viewModel.addToCart(product)
        
        // When
        viewModel.updateQuantity(1, 5)
        
        // Then
        val state = viewModel.uiState.value
        assertEquals(5, state.items[0].quantity)
    }
    
    @Test
    fun `clearCart removes all items`() {
        // Given
        val product1 = Product(id = 1, name = "PS5", price = 500000.0, category = "Consolas")
        val product2 = Product(id = 2, name = "Xbox", price = 450000.0, category = "Consolas")
        viewModel.addToCart(product1)
        viewModel.addToCart(product2)
        
        // When
        viewModel.clearCart()
        
        // Then
        val state = viewModel.uiState.value
        assertEquals(0, state.items.size)
    }
}