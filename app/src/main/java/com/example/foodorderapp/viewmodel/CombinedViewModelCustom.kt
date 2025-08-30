package com.example.foodorderapp.viewmodel

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel

class CombinedViewModelCustom(
    private val viewModelCustom: ViewModelCustom,
    private val shareViewModelSellerId: ShareViewModelSellerId,
    private val shareViewModelFoodId: ShareViewModelFoodId,
    private val viewModelGetInforUser: ViewModelGetInforUser
): ViewModel() {
        data class CombinedData(
            val foodId: String="",
            val sellerId: String="",
            val userId: String="",
            val priceSize: Int=0,
            val quantity: Int=0
        )
    private val _combinedData= MediatorLiveData<CombinedData>()
    val combinedData : MediatorLiveData<CombinedData> get()= _combinedData
    init {
        combinedData.value= CombinedData()
        _combinedData.addSource(shareViewModelFoodId.theoDoiFoodId){foodId->
            _combinedData.value=_combinedData.value?.copy(foodId = foodId)
        }
        _combinedData.addSource(shareViewModelSellerId.theoDoiSellerId){sellerId->
            _combinedData.value= _combinedData.value?.copy(sellerId = sellerId)
        }
        _combinedData.addSource(viewModelGetInforUser.theodoiInforUser){newUser->
            Log.d("CombinedViewModelCustom","Lấy được userId là: ${newUser.user_id}")
            _combinedData.value= _combinedData.value?.copy(userId = newUser.user_id)
        }
        _combinedData.addSource(viewModelCustom.theoDoiGiaSize){giaSize->
            _combinedData.value= _combinedData.value?.copy(priceSize = giaSize)
        }
        _combinedData.addSource(viewModelCustom.theoDoiSoLuong){soLuong->
            _combinedData.value= _combinedData.value?.copy(quantity = soLuong)
        }
    }

}