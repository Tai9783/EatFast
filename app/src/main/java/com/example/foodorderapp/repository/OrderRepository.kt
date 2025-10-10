package com.example.foodorderapp.repository

import android.util.Log
import com.example.foodorderapp.model.FoodItemCart

import com.example.foodorderapp.model.OrderFireStore
import com.example.foodorderapp.model.OrderStatus
import com.google.firebase.firestore.FirebaseFirestore

class OrderRepository {
   private val db= FirebaseFirestore.getInstance()
    fun createOrder(listFood: List<FoodItemCart>,totalPrice: Int ,onCallBack: (Boolean) -> Unit) {
        val sellerId = listFood[0].seller_id
        val userId = listFood[0].user_id
        val orderId = "${System.currentTimeMillis()}_${(1..999).random()}"
        val createdAt = com.google.firebase.Timestamp.now()
        val deliveryTime = 30
        val status = OrderStatus.PENDING
        val listFoodId= listFood.map { it.food_id }

        val order= OrderFireStore(orderId = orderId, userId = userId, sellerId = sellerId, createdAt = createdAt,
            listFood = listFoodId, totalPrice = totalPrice, deliveryTime = deliveryTime,
            status = status, cancleReason = "")
        db.collection("Orders").document(orderId).set(order)
            .addOnSuccessListener {
                onCallBack(true)
                Log.d("OrderRepository", "Đặt hàng thành công")
            }
            .addOnFailureListener {
                onCallBack(false)
                Log.d("OrderRepository", "Đặt hàng không thành công")
            }
    }
    fun updateQuantityStock(listFood: List<FoodItemCart>,onCallBack: (Boolean) -> Unit){
        db.runTransaction {transition->
          val docRefs= listFood.map { db.collection("Foods").document(it.food_id) } // lưu danh sách dưới dạng [ documentReference("Food/food001"),docmentReference("Food/food002")...] danh sachs này chứa các địa chỉ trỏ tới các món
            val snapShots= docRefs.map { transition.get(it) } // chuyển danh sách trên thành [ documentReference("Food/food001")=>{"food_id": "apple123", "name": "Táo Mỹ", "stockQuantity": 10...},docmentReference("Food/food002")=>"food_id": "orange456", "name": "Cam", "stockQuantity": 2...]


            //Lưu ý: Firestore Transaction: các thao tác đọc(get) phải được thực hiện trước khi viết update/set
            //Sau khi đọc xong các dữ liệu cần dùng thì cập nhật lại stockQuantity ( lưu ý
            for ((item,snap) in listFood.zip(snapShots)){
                val currentStock= snap.getLong("stockQuantity")?.toInt()?:0
                val newStock= currentStock- item.quantity
                transition.update(db.collection("Foods").document(item.food_id),"stockQuantity",newStock)
            }
            null

        }
            .addOnSuccessListener{
                onCallBack(true)
            }
            .addOnFailureListener {e->
                e.printStackTrace()
                onCallBack(false)
                Log.d("OrderRepository", "Cập nhật stockQuantity không thành công ${e.message}")
            }
    }

}