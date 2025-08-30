package com.example.foodorderapp.datasource.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.foodorderapp.model.CartOnRoom
import com.example.foodorderapp.model.FoodItemCart

@Dao
interface CartDao {
    @Query("""
         SELECT 
            c.cartItemId, c.cart_id, c.user_id, c.seller_id, c.food_id, 
            c.option_description, c.quantity, c.priceSize, c.checkBox,
            f.name_food AS tenMonAn, f.image_url AS anhMonAn, f.price AS giaMonAn,
            s.shop_name AS tenNhaHang
        FROM Carts c
        INNER JOIN Foods f   ON f.food_id   = c.food_id
        INNER JOIN Sellers s ON s.seller_id = c.seller_id
        WHERE c.user_id = :userId
        ORDER BY c.cartItemId
    """)
    fun getAllItem(userId:String): LiveData<List<FoodItemCart>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insertItem(item: FoodItemCart)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(item: List<FoodItemCart>)
    @Update
    suspend fun updateItem(item: FoodItemCart)

    @Delete
    suspend fun deleteItem(item:FoodItemCart)



    @Query("DELETE FROM Carts")
    suspend fun clearAll()


}