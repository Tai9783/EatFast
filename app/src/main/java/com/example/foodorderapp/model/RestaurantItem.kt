package com.example.foodorderapp.model

data class RestaurantItem(
    val sellerId: String="",
    val logoResId: String="",//logo nha hang
    val name: String="",
    val ratingScore: Double=0.0, //diem so (5.0)
    val address: String="",

)
