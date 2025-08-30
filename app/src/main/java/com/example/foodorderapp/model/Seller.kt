package com.example.foodorderapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Sellers")
data class Seller(
    @PrimaryKey var seller_id: String="",
    var user_id: String="",
    var shop_name: String=""
)
