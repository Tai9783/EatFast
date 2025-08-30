package com.example.foodorderapp.datasource.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodorderapp.model.Seller
@Dao
interface SellerDao {

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun insertAllSeller(seller: List<Seller>)


    @Query("DELETE FROM Sellers")
    suspend fun cleanAll()
}