package com.example.levelup.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.levelup.data.database.LevelUpDatabase
import com.example.levelup.data.local.DataStoreManager
import com.example.levelup.data.model.User
import com.example.levelup.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Estado de la pantalla de login
data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null,
    val currentUser: User? = null
)

// ViewModel para manejar el login y registro
class AuthViewModel(
    application: Application,
    private val userRepository: UserRepository,
    private val dataStoreManager: DataStoreManager
) : AndroidViewModel(application) {
    
    // Constructor secundario para mantener compatibilidad (si fuera necesario) o para pruebas
    // Pero el Factory es la mejor forma
    
    // Estado actual de la UI
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    init {
        // Ver si ya hay alguien logueado
        viewModelScope.launch {
            val isLoggedIn = dataStoreManager.isLoggedIn.first()
            if (isLoggedIn) {
                val userId = dataStoreManager.userId.first()
                if (userId != null) {
                    val user = userRepository.getUserById(userId)
                    _uiState.value = _uiState.value.copy(
                        isLoggedIn = true,
                        currentUser = user
                    )
                }
            }
        }
    }
    
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                val database = LevelUpDatabase.getDatabase(application)
                val repository = UserRepository(database.userDao())
                val dataStore = DataStoreManager(application)
                AuthViewModel(application, repository, dataStore)
            }
        }
    }
    
    // Función para hacer login
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                // Buscar el usuario en la base de datos
                val user = userRepository.login(email, password)
                if (user != null) {
                    // Guardar que está logueado
                    dataStoreManager.setLoggedIn(user.id)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        currentUser = user,
                        errorMessage = null
                    )
                } else {
                    // No se encontró el usuario
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Email o contraseña incorrectos"
                    )
                }
            } catch (e: Exception) {
                // Si hay algún error
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al iniciar sesión: ${e.message}"
                )
            }
        }
    }
    
    // Función para registrar un nuevo usuario
    fun register(name: String, email: String, password: String, country: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                // Validar que el email no esté vacío
                if (email.isBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "El email no puede estar vacío"
                    )
                    return@launch
                }
                
                // Verificar si el email ya existe
                val existingUser = userRepository.getUserByEmail(email.trim())
                if (existingUser != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Este email ya está registrado"
                    )
                    return@launch
                }
                
                // Crear el nuevo usuario
                val user = User(
                    email = email.trim(),
                    password = password, // En producción debería estar hasheado
                    name = name.trim(),
                    country = country.trim(),
                    createdAt = System.currentTimeMillis()
                )
                
                // Insertar el usuario
                val userId = userRepository.insertUser(user)
                
                if (userId > 0) {
                    // Obtener el usuario creado para confirmar
                    val newUser = userRepository.getUserById(userId)
                    if (newUser != null) {
                        // Guardar que está logueado
                        dataStoreManager.setLoggedIn(userId)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            currentUser = newUser,
                            errorMessage = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Error: No se pudo obtener el usuario creado"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Error: No se pudo crear el usuario"
                    )
                }
            } catch (e: Exception) {
                // Mostrar el error
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al registrar: ${e.message ?: "Error desconocido"}"
                )
            }
        }
    }
    
    // Cerrar sesión
    fun logout() {
        viewModelScope.launch {
            dataStoreManager.logout()
            _uiState.value = AuthUiState() // Resetear el estado
        }
    }
    
    // Actualizar perfil del usuario
    fun updateProfile(name: String, email: String, country: String, newPassword: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val currentUser = _uiState.value.currentUser
                if (currentUser == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "No hay usuario logueado"
                    )
                    return@launch
                }
                
                // Verificar si el email ya está en uso por otro usuario
                if (email != currentUser.email) {
                    val existingUser = userRepository.getUserByEmail(email.trim())
                    if (existingUser != null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Este email ya está registrado"
                        )
                        return@launch
                    }
                }
                
                // Actualizar el usuario
                if (country.isBlank()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "El país es requerido"
                    )
                    return@launch
                }
                
                val updatedUser = currentUser.copy(
                    name = name.trim(),
                    email = email.trim(),
                    country = country.trim(),
                    password = newPassword ?: currentUser.password
                )
                
                userRepository.updateUser(updatedUser)
                
                // Actualizar el estado con el usuario modificado
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentUser = updatedUser,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al actualizar perfil: ${e.message}"
                )
            }
        }
    }
    
    // Recargar datos del usuario
    fun refreshUser() {
        viewModelScope.launch {
            val userId = dataStoreManager.userId.first()
            if (userId != null) {
                val user = userRepository.getUserById(userId)
                _uiState.value = _uiState.value.copy(currentUser = user)
            }
        }
    }
    
    // Actualizar imagen de perfil
    fun updateProfileImage(imagePath: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val currentUser = _uiState.value.currentUser
                if (currentUser == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "No hay usuario logueado"
                    )
                    return@launch
                }
                
                // Actualizar el usuario con la nueva ruta de imagen
                val updatedUser = currentUser.copy(profileImagePath = imagePath)
                userRepository.updateUser(updatedUser)
                
                // Actualizar el estado con el usuario modificado
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentUser = updatedUser,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al actualizar imagen: ${e.message}"
                )
            }
        }
    }
}