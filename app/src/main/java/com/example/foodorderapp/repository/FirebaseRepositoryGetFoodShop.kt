package com.example.foodorderapp.repository

import android.util.Log
import com.example.foodorderapp.model.ItemMonAnSeller
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepositoryGetFoodShop {
    private val dbFood= FirebaseFirestore.getInstance().collection("Foods")
    private var listFood= mutableListOf<ItemMonAnSeller>()
    fun getFood(sellerId: String, onCallBack: (List<ItemMonAnSeller>) -> Unit){
        Log.d("FirebaseGetFood","Lấy được seller_id là: $sellerId")
        dbFood.get()
            .addOnSuccessListener {list->
                listFood.clear()
                for (item in list){
                    val idSeller= item.getString("seller_id") ?: ""
                    if(idSeller==sellerId){
                        val foodId= item.getString("food_id")?:""
                        val nameFood= item.getString("name_food") ?:""
                        val quantitySold= item.getLong("quantity_sold")?.toInt()?:0
                        val price= item.getLong("price")?.toInt() ?: 0
                        val imageFoodUrl= item.getString("image_url") ?: ""
                        val itemMonAn = ItemMonAnSeller(
                            nameFood = nameFood,
                            quantitySold = quantitySold,
                            price = price,
                            imageFoodUrl = imageFoodUrl,
                            likeCheck = false,
                            food_id = foodId

                        )
                        listFood.add(itemMonAn)
                    }
                }
                onCallBack(listFood)
            }

            .addOnFailureListener {
                onCallBack(emptyList())
            }
    }


}