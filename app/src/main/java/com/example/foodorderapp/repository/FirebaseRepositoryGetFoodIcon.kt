package com.example.foodorderapp.repository

import android.util.Log
import com.example.foodorderapp.model.IconFood
import com.example.foodorderapp.model.ItemFoodIcon
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepositoryGetFoodIcon {
    private val db= FirebaseFirestore.getInstance().collection("Foods")
    private val listFood= mutableListOf<ItemFoodIcon>()
    private var dem=0
    fun getFoodIcon(categotyType: String, onCallBack: (List<ItemFoodIcon>)-> Unit){
        db.get()
            .addOnSuccessListener {result->
                val matched= result.filter {
                    it.getString("category_type")==categotyType
                }
                if(matched.isEmpty())
                {
                    onCallBack(emptyList())
                    return@addOnSuccessListener
                }
                for (item in matched){
                        val sellerId= item.getString("seller_id") ?: ""
                        val nameFood= item.getString("name_food") ?: ""
                        val price= item.getLong("price")?.toInt() ?: 0
                        val imageFoodUrl= item.getString("image_url") ?: ""
                        FirebaseRepositoryBannerShop().getInforSeller(sellerId){list->
                            val nameShop= list.nameShop
                            val rating = list.rating

                            val foodIcon= ItemFoodIcon(
                                nameShop = nameShop,
                                rating = rating,
                                nameFood = nameFood,
                                price = price,
                                imageFoodUrl = imageFoodUrl
                            )
                            listFood.add(foodIcon)
                            dem++
                            if(dem==matched.size)
                                onCallBack(listFood)

                    }
                }


            }

    }

}