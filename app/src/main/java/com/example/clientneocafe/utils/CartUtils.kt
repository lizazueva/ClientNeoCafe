package com.example.clientneocafe.utils

import com.example.clientneocafe.model.DetailInfoProduct

object CartUtils {
    private val cartItems: MutableList<DetailInfoProduct> = mutableListOf()

    fun addItem(product: DetailInfoProduct) {
        val existingItem = cartItems.find { it.id == product.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            val newItem = product.copy(quantity = 1)
            cartItems.add(newItem)
        }
    }

    fun removeItem(product: DetailInfoProduct) {
        val existingItem = cartItems.find { it.id == product.id }

        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                existingItem.quantity--
            } else {
                cartItems.remove(existingItem)
            }
        }
    }


    fun getCartItems(): List<DetailInfoProduct>{
        return cartItems.toList()
    }        val cart = CartUtils.getCartItems()


    fun clearCartItems() {
        cartItems.clear()
    }

    fun isInCart(productId: Int): Boolean {
        return cartItems.any { it.id == productId }
    }

    fun getQuantity(productId: Int): Int {
        return cartItems.find { it.id == productId }?.quantity ?: 0
    }
}