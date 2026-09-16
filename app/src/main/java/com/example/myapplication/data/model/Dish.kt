package com.example.myapplication.data.model

data class Dish(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: String = "",
    val isAvailable: Boolean = true
)
