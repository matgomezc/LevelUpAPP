package com.example.levelup.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.levelup.data.dao.GoalDao
import com.example.levelup.data.dao.ProductDao
import com.example.levelup.data.dao.ProgressPhotoDao
import com.example.levelup.data.dao.UserDao
import com.example.levelup.data.model.Goal
import com.example.levelup.data.model.Product
import com.example.levelup.data.model.ProgressPhoto
import com.example.levelup.data.model.User

@Database(
    entities = [User::class, Goal::class, ProgressPhoto::class, Product::class],
    version = 3,
    exportSchema = false
)
abstract class LevelUpDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun goalDao(): GoalDao
    abstract fun progressPhotoDao(): ProgressPhotoDao
    abstract fun productDao(): ProductDao
    
    companion object {
        @Volatile
        private var INSTANCE: LevelUpDatabase? = null
        
        fun getDatabase(context: Context): LevelUpDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LevelUpDatabase::class.java,
                    DATABASE_NAME
                )
                .fallbackToDestructiveMigration() // Para desarrollo - permite migraciones destructivas
                .build()
                INSTANCE = instance
                instance
            }
        }
        
        const val DATABASE_NAME = "levelup_database"
    }
}

