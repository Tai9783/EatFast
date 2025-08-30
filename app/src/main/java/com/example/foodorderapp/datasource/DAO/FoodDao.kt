package com.example.foodorderapp.datasource.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodorderapp.model.Food
@Dao
interface FoodDao {
    @Query("select * from Foods")
    fun getAllItem():LiveData<List<Food>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFood(foods: List<Food>)


    @Query("DELETE FROM Foods")
    suspend fun cleanAll()
}