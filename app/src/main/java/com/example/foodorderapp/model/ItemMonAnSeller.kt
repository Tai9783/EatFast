package com.example.foodorderapp.model

data class ItemMonAnSeller(
    val nameFood: String="",
    val quantitySold: Int=0,
    val price : Int=0,
    val imageFoodUrl: String="",
    val likeCheck: Boolean=false,
    val food_id: String=""
)
