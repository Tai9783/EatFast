package com.example.foodorderapp.repository

import android.util.Size
import com.example.foodorderapp.model.SizeOptionFood
import com.example.foodorderapp.model.TasteOptions
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseReposityGetFoodOptions {
    private val listSizeOption= mutableListOf<SizeOptionFood>()
    private val listTaste = mutableListOf<TasteOptions>()
    private val db= FirebaseFirestore.getInstance().collection("FoodOptions")
    fun getSizeOption(foodId: String, onCallBack: (List<SizeOptionFood>,List<TasteOptions>)->Unit){
        db.whereEqualTo("food_id",foodId)
            .get()
            .addOnSuccessListener {result->
                if (!result.isEmpty){
                    listSizeOption.clear()
                    for(document in result){
                        val sizeList= document["sizes"] as? List<*> //List<Map<String,Int>>
                        val tasteList= document["flavor_options"] as? List<*>
                        sizeList?.forEach{item->
                            val map = item as? Map<*, *>
                            val name= map?.get("label") as? String ?:""
                            val price= (map?.get("price") as? Number)?.toInt() ?:0
                           listSizeOption.add(SizeOptionFood(nameSize = name, price = price))
                        }
                      tasteList?.forEach{item->
                            if(item is String)
                                listTaste.add(TasteOptions(nameTaste = item))
                        }
                    }
                }
                onCallBack(listSizeOption,listTaste)
            }
            .addOnFailureListener {
                onCallBack(emptyList(), emptyList())
            }
    }

}

