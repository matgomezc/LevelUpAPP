package com.example.levelup.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress_photos")
data class ProgressPhoto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val goalId: Long,
    val photoPath: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)

