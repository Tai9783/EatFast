package com.example.foodorderapp.model

import com.google.firebase.Timestamp

data class InforUser(
    val address: String ="",
    val avatar_url: String="",
    val create_at: Timestamp= Timestamp.now(),
    val full_name: String="",
    val phone: String="",
    val user_id: String="",
    val role: String="customer",
    val email: String ="",

)
