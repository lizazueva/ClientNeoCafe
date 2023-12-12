package com.example.clientneocafe.model.cart

data class CreateOrder(
    val in_an_institution: Boolean,
    val items: List<Item>,
    val spent_bonus_points: Int,
    val total_price: Int
) {
    data class Item(
        val item_id: Int,
        val quantity: Int,
        val is_ready_made_product: Boolean
    )
}