package com.example.foodorderapp.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.example.foodorderapp.model.Promotion

class DiffCallBackPromotion: DiffUtil.ItemCallback<Promotion>() {
    override fun areItemsTheSame(oldItem: Promotion, newItem: Promotion): Boolean {
            return oldItem.tenMonAn== newItem.tenMonAn
    }

    override fun areContentsTheSame(oldItem: Promotion, newItem: Promotion): Boolean {
        return oldItem==newItem
    }
}