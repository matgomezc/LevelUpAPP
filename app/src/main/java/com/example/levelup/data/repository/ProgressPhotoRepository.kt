package com.example.levelup.data.repository

import com.example.levelup.data.dao.ProgressPhotoDao
import com.example.levelup.data.model.ProgressPhoto

class ProgressPhotoRepository(private val photoDao: ProgressPhotoDao) {
    suspend fun insertPhoto(photo: ProgressPhoto): Long = photoDao.insertPhoto(photo)
    
    suspend fun getPhotosByGoalId(goalId: Long): List<ProgressPhoto> = photoDao.getPhotosByGoalId(goalId)
    
    suspend fun getPhotoById(photoId: Long): ProgressPhoto? = photoDao.getPhotoById(photoId)
}

