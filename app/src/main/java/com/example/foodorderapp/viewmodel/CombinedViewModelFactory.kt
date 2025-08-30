package com.example.foodorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CombinedViewModelFactory(
    private val viewModelCustom: ViewModelCustom,
    private val shareViewModelSellerId: ShareViewModelSellerId,
    private val shareViewModelFoodId: ShareViewModelFoodId,
    private val viewModelGetInforUser: ViewModelGetInforUser
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CombinedViewModelCustom::class.java)){
            return CombinedViewModelCustom(
                viewModelCustom,
                shareViewModelSellerId,
                shareViewModelFoodId,
                viewModelGetInforUser
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}