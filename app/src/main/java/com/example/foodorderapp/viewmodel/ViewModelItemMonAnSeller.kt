package com.example.foodorderapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderapp.model.ItemMonAn
import com.example.foodorderapp.model.ItemMonAnSeller
import com.example.foodorderapp.repository.FirebaseRepositoryGetFoodShop

class ViewModelItemMonAnSeller: ViewModel() {
    private val list= mutableListOf<ItemMonAnSeller>()
    private val _theoDoiListItemMonAn = MutableLiveData<List<ItemMonAnSeller>>()
    val theoDoiListItemMonAn: LiveData<List<ItemMonAnSeller>> get() = _theoDoiListItemMonAn

    fun getListMonAn(sellerId: String){
        Log.d("ViewModel","Laays được seller là: $sellerId")
        FirebaseRepositoryGetFoodShop().getFood(sellerId){listFood->
            _theoDoiListItemMonAn.value= listFood.toList()
        }
    }

}