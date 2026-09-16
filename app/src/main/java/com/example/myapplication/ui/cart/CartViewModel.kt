package com.example.myapplication.ui.cart

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.Dish
import com.example.myapplication.data.model.Order
import com.example.myapplication.data.model.OrderItem
import com.example.myapplication.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<OrderItem>>(emptyList())
    val items: StateFlow<List<OrderItem>> = _items.asStateFlow()

    fun addToCart(dish: Dish) {
        _items.update { currentItems ->
            val existingItem = currentItems.find { it.dishId == dish.id }
            if (existingItem != null) {
                currentItems.map {
                    if (it.dishId == dish.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                currentItems + OrderItem(dish.id, dish.name, 1, dish.price)
            }
        }
    }

    fun removeFromCart(dishId: String) {
        _items.update { currentItems ->
            currentItems.filterNot { it.dishId == dishId }
        }
    }

    fun checkout() {
        if (_items.value.isEmpty()) return
        
        val newOrder = Order(
            userId = "usuario_actual",
            items = _items.value,
            totalPrice = totalPrice
        )
        OrderRepository.placeOrder(newOrder)
        clearCart()
    }

    fun clearCart() {
        _items.value = emptyList()
    }

    val totalPrice: Double
        get() = _items.value.sumOf { it.price * it.quantity }
}
