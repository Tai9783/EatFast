package com.example.foodorderapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModelFoodId: ViewModel() {
    private val _theoDoiFoodId= MutableLiveData<String>()
    val theoDoiFoodId: LiveData<String> get()= _theoDoiFoodId

    fun updateFoodId(foodId: String){
        _theoDoiFoodId.value=foodId
    }
}