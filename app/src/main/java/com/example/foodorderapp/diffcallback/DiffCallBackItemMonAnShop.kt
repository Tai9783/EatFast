package com.example.foodorderapp.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.example.foodorderapp.model.ItemMonAnSeller

class DiffCallBackItemMonAnShop : DiffUtil.ItemCallback<ItemMonAnSeller>() {
    override fun areItemsTheSame(oldItem: ItemMonAnSeller, newItem: ItemMonAnSeller): Boolean {
        return oldItem.nameFood==newItem.nameFood
    }

    override fun areContentsTheSame(oldItem: ItemMonAnSeller, newItem: ItemMonAnSeller): Boolean {
        return oldItem== newItem
    }
}