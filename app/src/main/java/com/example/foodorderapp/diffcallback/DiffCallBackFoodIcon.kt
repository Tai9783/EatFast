package com.example.foodorderapp.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.example.foodorderapp.model.ItemFoodIcon

class DiffCallBackFoodIcon: DiffUtil.ItemCallback<ItemFoodIcon>() {
    override fun areItemsTheSame(oldItem: ItemFoodIcon, newItem: ItemFoodIcon): Boolean {
        return oldItem.nameFood== newItem.nameFood
    }

    override fun areContentsTheSame(oldItem: ItemFoodIcon, newItem: ItemFoodIcon): Boolean {
        return newItem== oldItem
    }
}