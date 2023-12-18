package com.example.clientneocafe.model

data class DetailInfoProduct(
    val category: Category,
    val compositions: List<Composition>?,
    val description: String,
    val id: Int,
    val image: String?,
    val is_available: Boolean,
    val name: String,
    val price: String,
    val is_ready_made_product: Boolean,
    var quantity: Int?,
    var quantityForCard: Int
) {
    data class Category(
        val id: Int?,
        val image: String?,
        val name: String
    )

    data class Composition(
        val id: Int?,
        val name: String?,
        val quantity: Int?
    )
}