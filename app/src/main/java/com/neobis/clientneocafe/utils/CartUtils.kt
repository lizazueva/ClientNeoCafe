package com.neobis.clientneocafe.utils

import com.neobis.clientneocafe.model.DetailInfoProduct
import com.neobis.clientneocafe.model.home.SearchResultResponse

object CartUtils {
    private val cartItems: MutableList<DetailInfoProduct> = mutableListOf()

    fun addItem(product: DetailInfoProduct) {
        val existingItem = cartItems.find { it.id == product.id }

        if (existingItem != null) {
            existingItem.quantityForCard++
        } else {
            val newItem = product.copy(quantityForCard = 1)
            cartItems.add(newItem)
        }
    }

    fun removeItem(product: DetailInfoProduct) {
        val existingItem = cartItems.find { it.id == product.id }

        if (existingItem != null) {
            if (existingItem.quantityForCard > 1) {
                existingItem.quantityForCard--
            } else {
                cartItems.remove(existingItem)
            }
        }
    }
    fun addItem(product: SearchResultResponse) {
        val existingItem = cartItems.find { it.id == product.id }

        if (existingItem != null) {
            existingItem.quantityForCard++
        } else {
            val newItem = DetailInfoProduct(
                category = DetailInfoProduct.Category(id = null, image = null, name = product.category_name),
                compositions = emptyList(),
                description = product.description,
                id = product.id,
                image = product.image,
                is_available = true,
                name = product.name,
                price = product.price.toString(),
                is_ready_made_product = product.is_ready_made_product,
                quantity = null,
                quantityForCard = 1
            )
            cartItems.add(newItem)
        }
    }
    fun removeItem(product: SearchResultResponse) {
        val existingItem = cartItems.find { it.id == product.id }

        if (existingItem != null) {
            if (existingItem.quantityForCard > 1) {
                existingItem.quantityForCard--
            } else {
                cartItems.remove(existingItem)
            }
        }
    }


    fun getCartItems(): List<DetailInfoProduct>{
        return cartItems.toList()
    }


    fun clearCartItems() {
        cartItems.clear()
    }

    fun isInCart(productId: Int): Boolean {
        return cartItems.any { it.id == productId }
    }

    fun getQuantity(productId: Int): Int {
        return cartItems.find { it.id == productId }?.quantityForCard ?: 0
    }
}