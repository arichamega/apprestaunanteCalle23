package com.example.myapplication.data.model

data class Order(
    val id: String = "",
    val userId: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val status: OrderStatus = OrderStatus.RECEIVED,
    val timestamp: Long = System.currentTimeMillis(),
    val tableNumber: Int? = null
)

data class OrderItem(
    val dishId: String = "",
    val dishName: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0
)

enum class OrderStatus(val displayName: String) {
    RECEIVED("Recibido"),
    PREPARING("En preparación"),
    READY("Listo"),
    DELIVERED("Entregado"),
    CANCELLED("Cancelado")
}
