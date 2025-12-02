package com.example.levelup.data.network.model

import com.example.levelup.data.model.Product

fun ApiProduct.toProduct(): Product {
    return Product(
        id = this.id.toLong(),
        name = this.title,
        price = this.price,
        category = this.category,
        description = this.description,
        imageUrl = this.image,
        stock = 10, // Valor por defecto ya que la API no provee stock
        createdAt = System.currentTimeMillis()
    )
}

fun List<ApiProduct>.toProductList(): List<Product> {
    return this.map { it.toProduct() }
}