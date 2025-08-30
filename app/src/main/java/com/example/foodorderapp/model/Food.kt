package com.example.foodorderapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Foods",
    foreignKeys = [
        ForeignKey(
            entity = Seller::class,             // bảng cha
            parentColumns = ["seller_id"],      // khóa chính bên bảng Seller
            childColumns = ["seller_id"],       // khóa ngoại bên bảng Food
            onDelete = ForeignKey.CASCADE       // nếu xóa seller thì bên bảng này cunngx xóa luôn
        )
    ],
    indices = [Index(value = ["seller_id"])]    // Tạo index cho seller để tăng tốc truy vấn
)
data class Food(
    @PrimaryKey
    @ColumnInfo(name = "food_id") var food_id: String="",
    var seller_id: String="",
    var category_type: String="",
    var image_url: String="",
    var name_food: String="",
    var price: Int=0,
    var quantity_sold: Int=0,
    )
