package com.example.foodorderapp.repository

import android.util.Log
import com.example.foodorderapp.datasource.DAO.CartDao
import com.example.foodorderapp.model.FoodItemCart
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepositoryCustom(private val cartDao: CartDao) {
    private val db= FirebaseFirestore.getInstance()
    fun getCartId(onCallBack:(String)->Unit){
       val counteref= db.collection("Counters").document("counter001")
        db.runTransaction{transaction->
            //lấy giá trị hiện tại trong bộ lưu trữ
            val snapshot=transaction.get(counteref)
            val currentCounter= snapshot.getLong("cartCounter")?:0
            val newCounter= currentCounter+1
            transaction.update(counteref,"cartCounter",newCounter)
            val newCartId= "cart"+String.format("%03d",newCounter)
            onCallBack(newCartId)
        }
    }
    fun addCart(item: FoodItemCart,onCallBack:(Boolean)->Unit){
        val cartRef= db.collection("Carts")
            .whereEqualTo("user_id",item.user_id)
            .whereEqualTo("cartItemId",item.cartItemId)
        cartRef.get()
            .addOnSuccessListener {querySnapshot->
                    if(!querySnapshot.isEmpty){
                        val existingDoc = querySnapshot.documents[0]
                        val existingId = existingDoc.id
                        db.collection("Carts").document(existingId)
                            .update("quantity",item.quantity)
                            .addOnSuccessListener {
                                onCallBack(true)
                            }
                    }
                else
                    {
                        val newItem= mapOf(
                            "cart_id" to item.cart_id,
                            "cartItemId" to item.cartItemId,
                            "food_id" to  item.food_id,
                            "option_description" to item.option_description,
                            "quantity" to item.quantity,
                            "seller_id" to item.seller_id,
                            "user_id" to item.user_id,
                            "priceSize" to item.priceSize,
                            "checkBox" to item.checkBox
                        )
                        db.collection("Carts").document(item.cart_id).set(newItem)
                            .addOnSuccessListener {
                                onCallBack(true)
                            }
                            .addOnFailureListener {
                                onCallBack(false)
                            }
                    }

            }
            .addOnFailureListener {
                onCallBack(false)
            }
    }

    suspend fun addCartAndRoom(item: FoodItemCart){
        cartDao.insertItem(item)
        addCart(item){success->
            Log.d("CartRepo", "Sync Firestore: $success")
        }
    }
}