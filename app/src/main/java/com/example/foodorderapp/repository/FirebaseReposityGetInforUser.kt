package com.example.foodorderapp.repository

import android.util.Log
import com.example.foodorderapp.model.InforUser
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseReposityGetInforUser {
       private  val db= FirebaseFirestore.getInstance().collection("Users")
    fun getInforUser(email:String, onCallBack: (InforUser)-> Unit){
        var item= InforUser("","")
        db.whereEqualTo("email",email.trim())
            .get()
            .addOnSuccessListener {result->
                    if(!result.isEmpty) {
                        val i = result.documents[0]
                            val userId= i.getString("user_id")?:""
                            val fullName = i.getString("full_name") ?: ""
                            val imageUrl = i.getString("avatar_url") ?: ""
                            val phone= i.getString("phone")?:""
                            val address= i.getString("address") ?: ""
                            item = InforUser(
                                user_id = userId,
                                email = email,
                                phone = phone,
                                full_name = fullName,
                                avatar_url = imageUrl,
                                address = address
                            )
                            onCallBack(item)
                    }
                else{
                        onCallBack(item)
                    }

            }
            .addOnFailureListener {
                onCallBack(item)
            }
    }
}