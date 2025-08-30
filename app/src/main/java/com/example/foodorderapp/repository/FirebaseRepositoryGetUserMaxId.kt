package com.example.foodorderapp.repository

import android.util.Log
import com.example.foodorderapp.model.InforUser
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepositoryGetUserMaxId {
    private val db= FirebaseFirestore.getInstance()
    private val userCollection= db.collection("Users")
    fun getUserMaxId(onResult: (String) -> Unit){
       userCollection.get()
            .addOnSuccessListener {result->
                var maxId= 0
                for (item in result){
                    val userId= item.getString("user_id") ?: continue
                    val number= userId.removePrefix("user").toIntOrNull() ?: continue
                    if(number>maxId)
                        maxId=number
                }
                val newIdNumber= maxId+1
                val newUserId= "user%03d".format(newIdNumber)
                onResult(newUserId)
            }
            .addOnFailureListener {
                onResult("")
            }
    }
    fun addUser(user: InforUser, oncomplete: (Boolean)-> Unit){
        if(user.user_id.isEmpty()) {
            Log.d("Firebase","user_id bị rỗng ")
            oncomplete(false)
            return
        }
            userCollection.document(user.user_id)
                .set(user)
                .addOnSuccessListener { oncomplete(true) }
                .addOnFailureListener { oncomplete(false) }

    }

}