package com.example.levelup.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.local.DataStoreManager
import com.example.levelup.data.model.Goal
import com.example.levelup.data.repository.GoalRepository
import com.example.levelup.services.LocationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Estado para la pantalla de detalle de meta
data class GoalDetailUiState(
    val goal: Goal? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentLocation: Pair<Double, Double>? = null // Latitud y longitud
)

// ViewModel para la pantalla de detalle de meta
class GoalDetailViewModel(application: Application) : AndroidViewModel(application) {
    
    private val goalRepository: GoalRepository
    private val locationService: LocationService
    private val dataStoreManager: DataStoreManager
    
    // Estado actual
    private val _uiState = MutableStateFlow(GoalDetailUiState())
    val uiState: StateFlow<GoalDetailUiState> = _uiState.asStateFlow()
    
    init {
        // Inicializar repositorios y servicios
        val database = com.example.levelup.data.database.LevelUpDatabase.getDatabase(application)
        goalRepository = GoalRepository(database.goalDao())
        locationService = LocationService(application)
        dataStoreManager = DataStoreManager(application)
    }
    
    // Cargar una meta por su ID
    fun loadGoal(goalId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Obtener la meta
                val goal = goalRepository.getGoalById(goalId)
                _uiState.value = _uiState.value.copy(
                    goal = goal,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar la meta: ${e.message}"
                )
            }
        }
    }
    
    // Actualizar el progreso de la meta
    fun updateProgress(newValue: Int) {
        viewModelScope.launch {
            try {
                val goal = _uiState.value.goal
                if (goal != null) {
                    goalRepository.updateGoalProgress(goal.id, newValue)
                    loadGoal(goal.id) // Recargar para actualizar
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al actualizar progreso: ${e.message}"
                )
            }
        }
    }
    
    // Obtener la ubicación actual del dispositivo
    fun getCurrentLocation() {
        viewModelScope.launch {
            try {
                val location = locationService.getCurrentLocation()
                if (location != null) {
                    // Guardar la latitud y longitud
                    _uiState.value = _uiState.value.copy(
                        currentLocation = Pair(location.latitude, location.longitude)
                    )
                }
            } catch (e: Exception) {
                // Si hay error, no pasa nada (la ubicación no es obligatoria)
            }
        }
    }
}

