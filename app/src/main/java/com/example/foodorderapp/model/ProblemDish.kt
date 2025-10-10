package com.example.foodorderapp.model

data class ProblemDish(
    val nameFood: String="",
    val issueMessage: String="",
    val currentStock: Int= 0,
    val newStock: Int= 0
)
