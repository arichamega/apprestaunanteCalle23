package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Order
import com.example.myapplication.data.model.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object OrderRepository {
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()
    
    private var orderCounter = 101

    fun placeOrder(order: Order) {
        val sequentialId = orderCounter.toString()
        orderCounter++
        _orders.update { it + order.copy(id = sequentialId) }
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        _orders.update { currentOrders ->
            currentOrders.map {
                if (it.id == orderId) it.copy(status = newStatus) else it
            }
        }
    }
}
