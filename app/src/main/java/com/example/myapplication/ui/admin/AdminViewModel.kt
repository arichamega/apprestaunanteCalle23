package com.example.myapplication.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Order
import com.example.myapplication.data.model.OrderStatus
import com.example.myapplication.data.repository.OrderRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {
    val orders: StateFlow<List<Order>> = OrderRepository.orders

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        viewModelScope.launch {
            OrderRepository.updateOrderStatus(orderId, newStatus)
        }
    }
}
