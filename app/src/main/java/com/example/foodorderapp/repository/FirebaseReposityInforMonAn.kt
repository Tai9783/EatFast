package com.example.foodorderapp.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseReposityInforMonAn {
    private val db= FirebaseFirestore.getInstance().collection("Foods")

    fun getInforMonAn(foodId: String, onCallBack:(String,Int,String)->Unit){
        db.whereEqualTo("food_id",foodId)
            .get()
            .addOnSuccessListener {result->
                if(!result.isEmpty){
                    val i= result.documents[0]
                    val nameFood= i.getString("name_food")?:""
                    val price= i.getLong("price")?.toInt()?:0
                    val imageUrl= i.getString("image_url")?:""
                    onCallBack(nameFood,price,imageUrl)
                } else {
                    // Chỉ gọi callback với dữ liệu rỗng khi thực sự không tìm thấy
                    onCallBack("",0,"")
                }
            }
            .addOnFailureListener {
                onCallBack("",0,"")
                Log.e("repository","Lấy thông tin món ăn không thành công")
            }
    }
}