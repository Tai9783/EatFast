package com.example.foodorderapp.repository

import android.util.Log
import com.example.foodorderapp.model.RestaurantItem
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepositoryShop {

   private val sellerList= mutableListOf<RestaurantItem>()
   private val db= FirebaseFirestore.getInstance().collection("Sellers")

      fun getAllSeller(onResult: (List<RestaurantItem>)-> Unit) { // Tìm hiểu lại chỗ lamda này
         db.get()
         .addOnSuccessListener { result ->
            sellerList.clear()
            for (document in result) {
               val sellerId=  document.getString("seller_id") ?: ""
               val logoShop = document.getString("shop_image_url") ?: ""
               val nameShop = document.getString("shop_name") ?: ""
               val rating = document.getDouble("rating") ?: 0.0
               val address = document.getString("address") ?: ""
               val item = RestaurantItem(
                  sellerId,
                  logoResId = logoShop,
                  name = nameShop,
                  ratingScore = rating,
                  address = address
               )
               sellerList.add(item)
            }
            onResult(sellerList)

         }
            .addOnFailureListener { e ->
               Log.e("FireStore", "Lỗi khi lấy Seller", e)
            }
      }
}