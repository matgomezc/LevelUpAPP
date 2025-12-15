package com.example.levelup.data.repository

import com.example.levelup.data.dao.UserDao
import com.example.levelup.data.model.User
import com.example.levelup.data.network.BackendClient
import com.example.levelup.data.network.model.LoginRequest
import com.example.levelup.data.network.model.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repositorio para manejar usuarios
class UserRepository(private val userDao: UserDao) {
    
    private val apiService = BackendClient.apiService
    
    // Insertar un nuevo usuario (Registro)
    suspend fun insertUser(user: User): Long {
        // Intentar registrar en backend primero
        try {
            val response = apiService.register(
                RegisterRequest(user.name, user.email, user.password)
            )
            // Si el backend responde ok, usamos el ID del backend si es posible, 
            // o actualizamos el usuario local con datos del backend
            // Por simplicidad, insertamos en local el usuario con la info original
            return userDao.insertUser(user)
        } catch (e: Exception) {
            // Fallback local si falla el backend (para demos o modo offline)
            // En un caso real estricto, deberíamos propagar el error
            e.printStackTrace()
            return userDao.insertUser(user)
        }
    }
    
    // Buscar usuario por email
    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)
    
    // Buscar usuario por ID
    suspend fun getUserById(id: Long): User? = userDao.getUserById(id)
    
    // Función de login - verifica email y contraseña
    suspend fun login(email: String, password: String): User? {
        // 1. Intentar login en Backend
        try {
            val response = apiService.login(LoginRequest(email, password))
            val apiUser = response.user
            
            // Login exitoso en backend. Sincronizamos/Guardamos en local
            // Verificamos si ya existe en local
            val localUser = userDao.getUserByEmail(email)
            
            if (localUser == null) {
                // Si no existe, lo creamos
                val newUser = User(
                    name = apiUser.name,
                    email = apiUser.email,
                    password = password, // Guardamos password para modo offline (idealmente hasheado)
                    createdAt = System.currentTimeMillis()
                )
                userDao.insertUser(newUser)
                return userDao.getUserByEmail(email)
            } else {
                // Si existe, actualizamos datos
                val updatedUser = localUser.copy(name = apiUser.name)
                userDao.updateUser(updatedUser)
                return updatedUser
            }
            
        } catch (e: Exception) {
            // 2. Si falla el backend (sin internet o servidor abajo), intentamos login local
            e.printStackTrace()
            
            val user = userDao.getUserByEmail(email)
            // Verificar si el usuario existe y la contraseña coincide
            return if (user != null && user.password == password) {
                user
            } else {
                null // No se encontró o la contraseña es incorrecta
            }
        }
    }
    
    // Actualizar un usuario
    suspend fun updateUser(user: User) = userDao.updateUser(user)
}