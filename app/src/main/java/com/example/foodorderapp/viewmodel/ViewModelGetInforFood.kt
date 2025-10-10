package com.example.foodorderapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderapp.model.Food
import com.example.foodorderapp.repository.FirebaseReposityInforMonAn

class ViewModelGetInforFood: ViewModel() {
    private val _selectFood= MutableLiveData<Food>()
    val selectFood: LiveData<Food> get()=_selectFood

    fun getInforFood(foodId: String){
        FirebaseReposityInforMonAn().getInforMonAn(foodId){newFood->
            _selectFood.value=newFood
        }

    }
}