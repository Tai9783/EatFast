package com.example.foodorderapp.repository

import android.util.Log
import com.example.foodorderapp.model.Food
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseReposityInforMonAn {
    private val db= FirebaseFirestore.getInstance().collection("Foods")
    fun getInforMonAn(foodId: String, onCallBack:(Food)->Unit){
        db.whereEqualTo("food_id",foodId)
            .get()
            .addOnSuccessListener {result->
                if(!result.isEmpty){
                    val i= result.documents[0]
                    val nameFood= i.getString("name_food")?:""
                    val price= i.getLong("price")?.toInt()?:0
                    val imageUrl= i.getString("image_url")?:""
                    val isAvailable= i.getBoolean("isAvailable")?:false
                    val stockQuantity= i.getLong("stockQuantity")?.toInt()?:0
                    val food= Food(name_food = nameFood, price = price, image_url = imageUrl, isAvailable = isAvailable, stockQuantity = stockQuantity)
                    onCallBack(food)
                } else {
                    // Chỉ gọi callback với dữ liệu rỗng khi thực sự không tìm thấy
                    onCallBack(Food())
                }
            }
            .addOnFailureListener {
                onCallBack(Food())
            }
    }
}