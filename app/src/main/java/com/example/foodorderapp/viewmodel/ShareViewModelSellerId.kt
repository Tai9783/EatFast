package com.example.foodorderapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModelSellerId: ViewModel() {
    private val _theoDoiSellerId=MutableLiveData<String>()
    val theoDoiSellerId: LiveData<String> get()= _theoDoiSellerId
    fun setSellerId(sellerId: String){
        _theoDoiSellerId.value=sellerId
        Log.d("ShareView","đã cập nhật đuoc có seller là $sellerId")
    }
}