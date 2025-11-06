package com.example.levelup.data.model

// Modelo para los items del carrito
data class CartItem(
    val product: Product,
    val quantity: Int = 1
) {
    val totalPrice: Double
        get() = product.price * quantity
}

