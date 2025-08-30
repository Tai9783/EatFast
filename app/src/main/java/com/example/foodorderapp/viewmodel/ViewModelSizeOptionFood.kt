package com.example.foodorderapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodorderapp.model.SizeOptionFood
import com.example.foodorderapp.model.TasteOptions
import com.example.foodorderapp.repository.FirebaseReposityGetFoodOptions

class ViewModelSizeOptionFood: ViewModel() {
    private val _theoDoiListSizeOptionFood= MutableLiveData<List<SizeOptionFood>>()
    val theoDoiListSizeOptionFood: LiveData<List<SizeOptionFood>> get()= _theoDoiListSizeOptionFood
    private val _theoDoiListTaste=MutableLiveData<List<TasteOptions>>()
    val theoDoiListTaste: LiveData<List<TasteOptions>> get()=_theoDoiListTaste

    fun getCustomizeOptionFood(foodId: String){
        FirebaseReposityGetFoodOptions().getSizeOption(foodId){newList,newListTast->
            _theoDoiListSizeOptionFood.value= newList.toList()
            _theoDoiListTaste.value= newListTast.toList()
        }
    }


}