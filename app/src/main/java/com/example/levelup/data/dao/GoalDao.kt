package com.example.levelup.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.levelup.data.model.Goal

@Dao
interface GoalDao {
    @Insert
    suspend fun insertGoal(goal: Goal): Long
    
    @Update
    suspend fun updateGoal(goal: Goal)
    
    @Query("SELECT * FROM goals WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getGoalsByUserId(userId: Long): List<Goal>
    
    @Query("SELECT * FROM goals WHERE id = :goalId LIMIT 1")
    suspend fun getGoalById(goalId: Long): Goal?
    
    @Query("DELETE FROM goals WHERE id = :goalId")
    suspend fun deleteGoal(goalId: Long)
}

