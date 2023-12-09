package com.example.clientneocafe.model.cart

data class CreateOrder(
    val in_an_institution: Boolean,
    val items: List<Item>,
    val spent_bonus_points: Int,
    val total_price: Int
) {
    data class Item(
        val item: Int,
        val quantity: Int
    )
}