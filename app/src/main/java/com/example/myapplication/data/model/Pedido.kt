package com.example.myapplication.data.model

data class Pedido(
    val id: String = "",
    val idUsuario: String = "",
    val items: List<ItemPedido> = emptyList(),
    val precioTotal: Double = 0.0,
    val estado: EstadoPedido = EstadoPedido.RECIBIDO,
    val estaPagado: Boolean = false,
    val marcaTiempo: Long = System.currentTimeMillis(),
    val numeroMesa: Int? = null
)

data class ItemPedido(
    val idPlato: String = "",
    val nombrePlato: String = "",
    val cantidad: Int = 1,
    val precio: Double = 0.0,
    val notas: String = ""
)

enum class EstadoPedido(val nombreMostrado: String) {
    RECIBIDO("Recibido"),
    EN_COCINA("En Cocina"),
    SERVIDO("Servido"),
    CANCELADO("Cancelado")
}
