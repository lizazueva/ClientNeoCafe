package com.neobis.clientneocafe.model.user

data class OrderDetail(
    val branch_name: String,
    val created_at: String,
    val id: Int,
    val items: List<Item>,
    val spent_bonus_points: Int,
    val total_price: String
) {
    data class Item(
        val item_category: String,
        val item_id: Int,
        val item_image: String?,
        val item_name: String,
        val item_price: String,
        val item_total_price: Double,
        val quantity: Int
    )
}