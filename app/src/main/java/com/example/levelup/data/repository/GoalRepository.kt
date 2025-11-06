package com.example.levelup.data.repository

import com.example.levelup.data.dao.GoalDao
import com.example.levelup.data.model.Goal

// Repositorio para manejar las metas
class GoalRepository(private val goalDao: GoalDao) {
    
    // Agregar una nueva meta
    suspend fun insertGoal(goal: Goal): Long = goalDao.insertGoal(goal)
    
    // Actualizar una meta
    suspend fun updateGoal(goal: Goal) = goalDao.updateGoal(goal)
    
    // Obtener todas las metas de un usuario
    suspend fun getGoalsByUserId(userId: Long): List<Goal> = goalDao.getGoalsByUserId(userId)
    
    // Obtener una meta por ID
    suspend fun getGoalById(goalId: Long): Goal? = goalDao.getGoalById(goalId)
    
    // Eliminar una meta
    suspend fun deleteGoal(goalId: Long) = goalDao.deleteGoal(goalId)
    
    // Actualizar el progreso de una meta
    suspend fun updateGoalProgress(goalId: Long, newValue: Int) {
        val goal = goalDao.getGoalById(goalId)
        goal?.let {
            // Actualizar el valor actual y verificar si se completó
            val updatedGoal = it.copy(
                currentValue = newValue,
                isCompleted = newValue >= it.targetValue // Se completa si alcanza el objetivo
            )
            goalDao.updateGoal(updatedGoal)
        }
    }
}

