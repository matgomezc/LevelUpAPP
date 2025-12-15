package com.example.levelup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Modelo de producto para el catálogo
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String, // Nombre del producto
    val price: Double, // Precio del producto
    val category: String, // Categoría del producto
    val description: String = "", // Descripción opcional
    val imageUrl: String = "", // URL de imagen (opcional)
    val stock: Int = 0, // Cantidad en stock
    val createdAt: Long = 0L // Se establecerá al crear el producto
)

