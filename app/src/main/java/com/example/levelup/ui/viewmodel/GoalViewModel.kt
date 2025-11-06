package com.example.levelup.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.local.DataStoreManager
import com.example.levelup.data.model.Goal
import com.example.levelup.data.repository.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Estado para mostrar las metas
data class GoalUiState(
    val goals: List<Goal> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// ViewModel para manejar las metas
class GoalViewModel(application: Application) : AndroidViewModel(application) {
    
    private val goalRepository: GoalRepository
    private val dataStoreManager: DataStoreManager
    
    // Estado actual
    private val _uiState = MutableStateFlow(GoalUiState())
    val uiState: StateFlow<GoalUiState> = _uiState.asStateFlow()
    
    init {
        // Inicializar
        val database = com.example.levelup.data.database.LevelUpDatabase.getDatabase(application)
        goalRepository = GoalRepository(database.goalDao())
        dataStoreManager = DataStoreManager(application)
        
        // Cargar las metas al iniciar
        loadGoals()
    }
    
    // Cargar todas las metas del usuario
    private fun loadGoals() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val userId = dataStoreManager.userId.first()
                if (userId != null) {
                    // Obtener las metas de la base de datos
                    val goals = goalRepository.getGoalsByUserId(userId)
                    _uiState.value = _uiState.value.copy(
                        goals = goals,
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar metas: ${e.message}"
                )
            }
        }
    }
    
    // Agregar una nueva meta
    fun addGoal(
        title: String,
        description: String,
        targetValue: Int,
        category: String
    ) {
        viewModelScope.launch {
            try {
                val userId = dataStoreManager.userId.first()
                if (userId != null) {
                    // Crear la nueva meta
                    val goal = Goal(
                        userId = userId,
                        title = title,
                        description = description,
                        targetValue = targetValue,
                        category = category
                    )
                    // Guardar en la base de datos
                    goalRepository.insertGoal(goal)
                    // Recargar las metas
                    loadGoals()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al agregar meta: ${e.message}"
                )
            }
        }
    }
    
    // Actualizar el progreso de una meta
    fun updateGoalProgress(goalId: Long, newValue: Int) {
        viewModelScope.launch {
            try {
                goalRepository.updateGoalProgress(goalId, newValue)
                loadGoals() // Recargar para actualizar la UI
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al actualizar progreso: ${e.message}"
                )
            }
        }
    }
    
    // Eliminar una meta
    fun deleteGoal(goalId: Long) {
        viewModelScope.launch {
            try {
                goalRepository.deleteGoal(goalId)
                loadGoals()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al eliminar meta: ${e.message}"
                )
            }
        }
    }
    
    // Refrescar las metas
    fun refreshGoals() {
        loadGoals()
    }
}

