package com.example.foodorderapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.foodorderapp.datasource.DAO.CartDao
import com.example.foodorderapp.datasource.DAO.FoodDao
import com.example.foodorderapp.datasource.DAO.SellerDao
import com.example.foodorderapp.model.Food
import com.example.foodorderapp.model.FoodItemCart
import com.example.foodorderapp.model.Seller
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.tasks.await

class FirebaseReposityGetCart(private val cartDao: CartDao,
                            private val sellerDao: SellerDao,
                            private val foodDao: FoodDao
) {

    private val db= FirebaseFirestore.getInstance()

    //đồng bộ các bảng liên quan lên room
    suspend fun syncData(userId: String){
        val sellers= db.collection("Sellers").get().await().toObjects(Seller::class.java)
        val foods= db.collection("Foods").get().await().toObjects(Food::class.java)
        val carts=db.collection("Carts")
            .whereEqualTo("user_id",userId).get()
            .await().map { it.toObject(FoodItemCart::class.java)}
        sellerDao.cleanAll()
        sellerDao.insertAllSeller(sellers)

        foodDao.cleanAll()
        foodDao.insertAllFood(foods)

        cartDao.clearAll()
        cartDao.insertAll(carts)
    }
    fun getdsRooom(userId:String): LiveData<List<FoodItemCart>> = cartDao.getAllItem(userId)
    fun getDsFood(): LiveData<List<Food>> = foodDao.getAllItem()

    suspend fun updateItem(item: FoodItemCart){
        cartDao.updateItem(item)

        val cartId= item.cart_id
        val docRef= db.collection("Carts")
            .document(cartId)

        //update số lượng món ăn
        docRef.update("quantity",item.quantity)
            .addOnSuccessListener {

            }
            .addOnFailureListener {e->
                Log.d("FirebaseReposityGetCart","Lỗi Update ${e.message}")
            }

    }


    suspend fun deleteItem(item:FoodItemCart){
        cartDao.deleteItem(item)

        val cartId=item.cart_id
        val docRef= db.collection("Carts")
            .document(cartId)

        docRef.delete()
    }



}