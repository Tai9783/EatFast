package com.example.foodorderapp.model

data class CartOnRoom(
    val cartItemId: String,
    val cart_id: String,
    val user_id: String,
    val seller_id: String,
    val food_id: String,
    val option_description: String,
    val quantity: Int,
    val priceSize: Int,
    val checkBox: Boolean,

    // từ Foods
    val nameFood: String,
    val imageUrl: String,
    val basePrice: Int,

    // từ Sellers
    val shopName: String
)
