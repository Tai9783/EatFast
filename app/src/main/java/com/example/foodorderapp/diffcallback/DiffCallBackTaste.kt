package com.example.foodorderapp.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.example.foodorderapp.model.TasteOptions

class DiffCallBackTaste: DiffUtil.ItemCallback<TasteOptions>() {
    override fun areItemsTheSame(oldItem: TasteOptions, newItem: TasteOptions): Boolean {
        return oldItem.nameTaste== newItem.nameTaste
    }

    override fun areContentsTheSame(oldItem: TasteOptions, newItem: TasteOptions): Boolean {
        return oldItem== newItem
    }
}