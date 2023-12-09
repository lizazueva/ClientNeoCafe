package com.example.clientneocafe.utils

import com.example.clientneocafe.model.home.Category

data class CartItem(
    val productId: Int,
    val productName: String,
    val productPrice: String,
    val productCategory: String,
    val discr: String,
    val image: String?,
    var quantity: Int
)

