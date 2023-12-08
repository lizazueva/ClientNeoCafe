package com.example.clientneocafe.utils

import com.example.clientneocafe.model.DetailInfoProduct

object CartUtils {
    private val cartItems: MutableList<CartItem> = mutableListOf()

    fun addItem(product: DetailInfoProduct) {
        val existingItem = cartItems.find { it.productId == product.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            // Если товара еще нет в корзине, добавляем новый элемент
            val newItem = CartItem(
                productId = product.id,
                productName = product.name,
                productPrice = product.price,
                discr = product.description,
                image = product.image,
                quantity = 1
            )
            cartItems.add(newItem)
        }
    }

    fun removeItem(productId: Int) {
        val existingItem = cartItems.find { it.productId == productId }
        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                existingItem.quantity--
            } else {
                cartItems.remove(existingItem)
            }
        }

    }

    fun getCartItems(): List<CartItem> {
        return cartItems.toList()
    }

    fun isInCart(productId: Int): Boolean {
        return cartItems.any { it.productId == productId }
    }

    fun getQuantity(productId: Int): Int {
        return cartItems.find { it.productId == productId }?.quantity ?: 0
    }
}