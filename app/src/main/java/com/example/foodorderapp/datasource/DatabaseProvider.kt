package com.example.foodorderapp.datasource

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodorderapp.datasource.DAO.AppDatabase

class DatabaseProvider private constructor() {
    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cart_database"
                )
                    .fallbackToDestructiveMigration() // Xóa DB cũ, tạo mới nếu version thay đổi
                    .build()
                    .also { instance = it }
            }
            return instance!!
        }
    }
}