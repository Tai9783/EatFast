package com.example.foodorderapp.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.example.foodorderapp.model.FoodItemCart
import com.example.foodorderapp.model.ShoppingCartItemMonAn

class DiffCallBackItemMonAnShoppingCart :DiffUtil.ItemCallback<FoodItemCart>() {
    override fun areItemsTheSame(
        oldItem: FoodItemCart,
        newItem: FoodItemCart
    ): Boolean {
        return oldItem.cartItemId == newItem.cartItemId
    }

    override fun areContentsTheSame(
        oldItem: FoodItemCart,
        newItem: FoodItemCart
    ): Boolean {
       return oldItem==newItem
    }
}