package com.example.levelup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Modelo de usuario para la base de datos
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val password: String, // Nota: en producción debería estar hasheado, pero por ahora así
    val name: String,
    val country: String = "",
    val profileImagePath: String? = null, // Ruta de la imagen de perfil
    val createdAt: Long = 0L // Se establecerá al crear el usuario
)

