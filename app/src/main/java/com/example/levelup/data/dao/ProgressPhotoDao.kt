package com.example.levelup.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.levelup.data.model.ProgressPhoto

@Dao
interface ProgressPhotoDao {
    @Insert
    suspend fun insertPhoto(photo: ProgressPhoto): Long
    
    @Query("SELECT * FROM progress_photos WHERE goalId = :goalId ORDER BY timestamp DESC")
    suspend fun getPhotosByGoalId(goalId: Long): List<ProgressPhoto>
    
    @Query("SELECT * FROM progress_photos WHERE id = :photoId LIMIT 1")
    suspend fun getPhotoById(photoId: Long): ProgressPhoto?
}

