package com.example.clientneocafe.model.user

data class Orders(
    val closed_orders: List<ClosedOrder>,
    val opened_orders: List<OpenedOrder>
) {
    data class ClosedOrder(
        val branch_name: String,
        val created_at: String,
        val id: Int,
        val spent_bonus_points: Int,
        val branch__image: String?,
        val order_items: String,
        val total_price: String
    )

    data class OpenedOrder(
        val branch_name: String,
        val created_at: String,
        val id: Int,
        val spent_bonus_points: Int,
        val branch__image: String?,
        val order_items: String,
        val total_price: String
    )
}