package com.example.foodorderapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Carts",
    foreignKeys = [
        ForeignKey(
            entity = Food::class,           //bảng cha
            parentColumns = ["food_id"],    // khóa chính bên bảng Food
            childColumns = ["food_id"],     // khóa ngoại bên bảng FoodItemCart
            onDelete = ForeignKey.CASCADE   // Nếu xoá món ăn thì xóa luôn món đó bên FoodItemCart
        ),
        ForeignKey(
            entity = Seller::class,             // bảng cha
            parentColumns = ["seller_id"],      // khóa chính bên bảng Seller
            childColumns = ["seller_id"],       // khóa ngoại bên bảng Food
            onDelete = ForeignKey.CASCADE       // nếu xóa seller thì bên bảng này cunngx xóa luôn
        )
    ],
    indices = [Index(value = ["food_id"]) ,
    Index(value = ["seller_id"])]// Tạo index cho food_id để tăng tốc truy vấn

)
data class FoodItemCart(
    @PrimaryKey var cartItemId: String="",
    var cart_id: String="",
    var food_id: String="", // khóa ngoại liên kết bên bảng Foods
    var option_description: String="",// vừa lưu trên Firebase vừa phục vụ trên UI(ghi chú)
    var quantity: Int=0,   //vừa lưu trên Firebase vừa phục vụ trên UI(demSoLuong)
    var seller_id: String="",
    var user_id: String="",
    var priceSize: Int=0,
    var checkBox: Boolean = false,
    // phucj vụ trên UI, không lưu trên FireStore
    var anhMonAn: String = "",
    var tenMonAn: String = "",
    var tenNhaHang: String = "",
    var giaMonAn: Int = 0
)
