package com.example.foodorderapp.repository

import android.util.Log
import com.example.foodorderapp.model.InforBannerShop
import com.example.foodorderapp.model.ItemMonAnSeller
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseRepositoryBannerShop {

    private val dbSeller = FirebaseFirestore.getInstance().collection("Sellers")

    fun getInforSeller(sellerId: String, callback: (InforBannerShop) -> Unit) {
        dbSeller.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.getString("seller_id") ?: ""
                    if (id == sellerId) {
                        val nameShop = document.getString("shop_name") ?: ""
                        val logoShop = document.getString("shop_image_url") ?: ""
                        val bannerShop = document.getString("shop_imagebanner_url") ?: ""
                        val totalReviews = document.getLong("total_reviews")?.toInt() ?: 0
                        val rating = document.getDouble("rating")?.toFloat() ?: 0f


                        val sellerInfor = InforBannerShop(
                            sellerId = id,
                            nameShop = nameShop,
                            logoShop = logoShop,
                            bannerShop = bannerShop,
                            totalReviews = totalReviews,
                            rating = rating
                        )
                        callback(sellerInfor)
                        return@addOnSuccessListener
                    }
                }
            }
            .addOnFailureListener {
                callback(InforBannerShop())
            }
    }


}