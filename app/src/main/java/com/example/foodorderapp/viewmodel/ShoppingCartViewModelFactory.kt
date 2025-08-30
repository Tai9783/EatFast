package com.example.foodorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodorderapp.repository.FirebaseReposityGetCart

class ShoppingCartViewModelFactory(
    private val repository: FirebaseReposityGetCart

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelShoppingcart::class.java)) {
            return ViewModelShoppingcart(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}