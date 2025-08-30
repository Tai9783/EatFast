package com.example.foodorderapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderapp.R
import com.example.foodorderapp.model.Promotion

class ViewModelPromotion: ViewModel() {
    private val list = mutableListOf<Promotion>()
    private val _theodoi=MutableLiveData<List<Promotion>>()
    val theoidoi: LiveData<List<Promotion>> get()= _theodoi
    fun initData(){
        list.addAll(
            listOf(
                Promotion(30,"chỉ hôm nay","Bánh burger thịt bò đặc biệt!", R.drawable.promotion_burger_bo),
                Promotion(20,"cuối tuần","Pizza hải sản cao cấp", R.drawable.pizza_haisan),
                Promotion(10,"thứ 3 và thứ 5","Gà rán với sốt cay đặt biệt", R.drawable.promotion_garan),
                Promotion(10,"chỉ giảm thứ 2","Phở bò tái chanh thơm ngon", R.drawable.promotion_pho),

            )
        )
        _theodoi.value= list.toList()
    }

}