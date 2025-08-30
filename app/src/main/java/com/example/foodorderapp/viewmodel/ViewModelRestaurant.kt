package com.example.foodorderapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderapp.model.RestaurantItem
import com.example.foodorderapp.repository.FirebaseRepositoryShop

class ViewModelRestaurant: ViewModel() {
    private val firebaseRepositoryShop= FirebaseRepositoryShop()
    private val _restaurant = MutableLiveData<List<RestaurantItem>>()
    val restaurant: LiveData<List<RestaurantItem>> get()=_restaurant

    fun fetchRestaurant(){
        Log.d("ViewModelRestaurant","Đang bên ViewModelRestaurant nè")
        firebaseRepositoryShop.getAllSeller { listShopNew->
            Log.d("lấy dữ liệu xong","lấy dữ liệu xong bây giờ gàn vào danh sách nè")
            _restaurant.value= listShopNew

        }
    }
}