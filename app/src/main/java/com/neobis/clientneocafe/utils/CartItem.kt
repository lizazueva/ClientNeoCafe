package com.neobis.clientneocafe.utils

data class CartItem(
    val productId: Int,
    val productName: String,
    val productPrice: String,
    val productCategory: String,
    val discr: String,
    val image: String?,
    var quantity: Int
)

