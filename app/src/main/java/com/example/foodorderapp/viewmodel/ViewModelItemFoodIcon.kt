package com.example.foodorderapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderapp.model.ItemFoodIcon
import com.example.foodorderapp.repository.FirebaseRepositoryGetFoodIcon

class ViewModelItemFoodIcon: ViewModel() {
    private val _theoDoiListFoodIcon=MutableLiveData<List<ItemFoodIcon>>()
    val theoDoiListFoodIcon: LiveData<List<ItemFoodIcon>> get()= _theoDoiListFoodIcon
    fun getListFoodIcon(categoryType: String){
        FirebaseRepositoryGetFoodIcon().getFoodIcon(categoryType){listNew->

         _theoDoiListFoodIcon.value= listNew.toList()
            Log.d("jkhfd","laays dduwojc ${listNew.size}")
        }
    }
}