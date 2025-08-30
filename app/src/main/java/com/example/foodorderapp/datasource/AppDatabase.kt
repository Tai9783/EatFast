package com.example.foodorderapp.datasource.DAO

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodorderapp.model.Food
import com.example.foodorderapp.model.FoodItemCart
import com.example.foodorderapp.model.Seller

@Database(entities = [FoodItemCart::class,Food::class,
    Seller::class   ], version = 6, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun foodDao(): FoodDao
    abstract fun sellerDao(): SellerDao

}