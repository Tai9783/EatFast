package com.example.foodorderapp.model

import com.google.firebase.database.Exclude
import java.time.LocalDate
import java.time.LocalTime

data class Order(
    // lưu trên firestore
    val orderId: String="",
    val userId: String="",
    val sellerId: String="",
    val createdAt: com.google.firebase.Timestamp?= null,
    val listFood: List<String> = emptyList(),
    val totalPrice: Int=0,
    val deliveryTime: Int=0,
    val status: OrderStatus= OrderStatus.PENDING,
    val cancleReason: String = "",
    //phuc vu tren UI
    @get:Exclude val shopName: String,
    @get:Exclude val orderDate: LocalDate,
    @get:Exclude val orderTime: LocalTime,
    @get:Exclude val listMonAn:  List<FoodItemOrderDangGiao> = emptyList(),
    @get:Exclude val address: String="",
    @get:Exclude val phoneNumber: String=""

)
data class OrderFireStore(
    val orderId: String="",
    val userId: String="",
    val sellerId: String="",
    val createdAt: com.google.firebase.Timestamp?= null,
    val listFood: List<String> = emptyList(),
    val totalPrice: Int=0,
    val deliveryTime: Int=0,
    val status: OrderStatus= OrderStatus.PENDING,
    val cancleReason: String = "",
)
