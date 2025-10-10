package com.example.foodorderapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorderapp.model.FoodItemCart
import com.example.foodorderapp.model.ProblemDish
import com.example.foodorderapp.repository.FirebaseReposityInforMonAn
import com.example.foodorderapp.repository.OrderRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelOrder : ViewModel() {

    private val _orderResult= MutableSharedFlow<OrderResult>()
    val orderResult: MutableSharedFlow<OrderResult> get()= _orderResult



    private val _lisExeededStockFoods = MutableStateFlow<List<ProblemDish>>(emptyList())
    val listExeededStockFoods: StateFlow<List<ProblemDish>>  get() = _lisExeededStockFoods

    private fun createOrder(listFood: List<FoodItemCart>, totalPrice: Int) {
        OrderRepository().createOrder(listFood, totalPrice) { success ->
            viewModelScope.launch {
                _orderResult.emit(OrderResult.Success(success))
            }
        }
    }
    fun checkStockAndCreateOrder(listFood: List<FoodItemCart>, totalPrice: Int) {
        val exceededStockFoods: MutableList<ProblemDish> = mutableListOf()
        var proceessedCount=0
            for (item in listFood) {
                FirebaseReposityInforMonAn().getInforMonAn(item.food_id) { newFood ->
                    proceessedCount++
                    if(!newFood.isAvailable || item.quantity>newFood.stockQuantity) {
                        val currentStock= newFood.stockQuantity
                        val newStock= item.quantity
                        val nameFood= item.tenMonAn
                        val issueMessage= "Chỉ còn $currentStock suất (bạn đặt $newStock suất)"

                        exceededStockFoods.add(ProblemDish(nameFood,issueMessage,currentStock,newStock))
                    }
                    if (proceessedCount==listFood.size){
                        if (exceededStockFoods.isEmpty()){
                           OrderRepository().updateQuantityStock(listFood){isCheck->
                                if (isCheck) {
                                    createOrder(listFood, totalPrice)
                                }
                               else{
                                   viewModelScope.launch {

                                       _orderResult.emit(OrderResult.Failed(exceededStockFoods))
                                   }
                                }
                            }
                        }
                        else
                            viewModelScope.launch {
                                _lisExeededStockFoods.value = exceededStockFoods
                                _orderResult.emit(OrderResult.Failed(exceededStockFoods))
                            }
                    }
                }

            }

    }
    fun clearExcessStock(){
        _lisExeededStockFoods.value= emptyList()

    }

    sealed class OrderResult{
        data class Success(val isCheck: Boolean): OrderResult()
        data class  Failed(val problemFoods: List<ProblemDish>) : OrderResult()
    }

}