package com.example.foodorderapp.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.example.foodorderapp.model.SizeOptionFood

class DiffCallBackSizeOptionFood: DiffUtil.ItemCallback<SizeOptionFood>() {
    override fun areItemsTheSame(oldItem: SizeOptionFood, newItem: SizeOptionFood): Boolean {
        return oldItem.nameSize==newItem.nameSize
    }

    override fun areContentsTheSame(oldItem: SizeOptionFood, newItem: SizeOptionFood): Boolean {
       return oldItem==newItem
    }
}