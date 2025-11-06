package com.example.levelup.data.repository

import com.example.levelup.data.dao.UserDao
import com.example.levelup.data.model.User

// Repositorio para manejar usuarios
class UserRepository(private val userDao: UserDao) {
    
    // Insertar un nuevo usuario
    suspend fun insertUser(user: User): Long = userDao.insertUser(user)
    
    // Buscar usuario por email
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    
    // Buscar usuario por ID
    suspend fun getUserById(id: Long): User? = userDao.getUserById(id)
    
    // Función de login - verifica email y contraseña
    suspend fun login(email: String, password: String): User? {
        val user = userDao.getUserByEmail(email)
        // Verificar si el usuario existe y la contraseña coincide
        return if (user != null && user.password == password) {
            user
        } else {
            null // No se encontró o la contraseña es incorrecta
        }
    }
    
    // Actualizar un usuario
    suspend fun updateUser(user: User) = userDao.updateUser(user)
}

