package com.example.levelup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Modelo de meta/objetivo
@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long, // ID del usuario que creó la meta
    val title: String, // Título de la meta
    val description: String, // Descripción opcional
    val targetValue: Int, // Valor objetivo (ej: 100)
    val currentValue: Int = 0, // Valor actual (ej: 50)
    val category: String, // Categoría de la meta
    val createdAt: Long = System.currentTimeMillis(), // Fecha de creación
    val isCompleted: Boolean = false // Si ya se completó
)

