package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.model.EstadoPedido
import com.example.myapplication.data.model.Pedido
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object RepositorioPedidos {
    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos.asStateFlow()
    
    private var contadorPedidos = 101

    private val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (_: Exception) {
            null
        }
    }

    fun realizarPedido(pedido: Pedido) {
        val idSecuencial = contadorPedidos.toString()
        contadorPedidos++
        val nuevoPedido = pedido.copy(id = idSecuencial)
        
        _pedidos.update { it + nuevoPedido }

        try {
            val datosPedido = hashMapOf(
                "id" to idSecuencial,
                "idUsuario" to nuevoPedido.idUsuario,
                "precioTotal" to nuevoPedido.precioTotal,
                "numeroMesa" to (nuevoPedido.numeroMesa ?: 0),
                "estado" to nuevoPedido.estado.name,
                "nombreEstado" to nuevoPedido.estado.nombreMostrado,
                "estaPagado" to nuevoPedido.estaPagado,
                "marcaTiempo" to nuevoPedido.marcaTiempo,
                "items" to nuevoPedido.items.map { item ->
                    hashMapOf(
                        "idPlato" to item.idPlato,
                        "nombrePlato" to item.nombrePlato,
                        "cantidad" to item.cantidad,
                        "precio" to item.precio,
                        "notas" to item.notas
                    )
                }
            )

            firestore?.collection("orders")
                ?.document("order_$idSecuencial")
                ?.set(datosPedido)
                ?.addOnSuccessListener {
                    Log.d("Firestore", "✅ Comanda guardada con éxito en Firestore: order_$idSecuencial")
                }
                ?.addOnFailureListener { e ->
                    Log.e("Firestore", "❌ Error al guardar en Firestore: ${e.message}", e)
                }
        } catch (e: Exception) {
            Log.e("Firestore", "Excepción al enviar orden a Firestore", e)
        }
    }

    fun actualizarEstadoPedido(idPedido: String, nuevoEstado: EstadoPedido) {
        _pedidos.update { pedidosActuales ->
            pedidosActuales.map {
                if (it.id == idPedido) it.copy(estado = nuevoEstado) else it
            }
        }

        try {
            val actualizaciones = hashMapOf<String, Any>(
                "estado" to nuevoEstado.name,
                "nombreEstado" to nuevoEstado.nombreMostrado
            )
            firestore?.collection("orders")
                ?.document("order_$idPedido")
                ?.update(actualizaciones)
                ?.addOnSuccessListener {
                    Log.d("Firestore", "✅ Estado actualizado en Firestore: $idPedido -> ${nuevoEstado.name}")
                }
                ?.addOnFailureListener { e ->
                    Log.e("Firestore", "❌ Error al actualizar estado en Firestore: ${e.message}", e)
                }
        } catch (e: Exception) {
            Log.e("Firestore", "Excepción al actualizar estado en Firestore", e)
        }
    }

    fun marcarComoPagado(idPedido: String) {
        _pedidos.update { pedidosActuales ->
            pedidosActuales.map {
                if (it.id == idPedido) it.copy(estaPagado = true) else it
            }
        }

        try {
            firestore?.collection("orders")
                ?.document("order_$idPedido")
                ?.update("estaPagado", true)
                ?.addOnSuccessListener {
                    Log.d("Firestore", "✅ Pago registrado en Firestore para orden: $idPedido")
                }
                ?.addOnFailureListener { e ->
                    Log.e("Firestore", "❌ Error al actualizar pago en Firestore: ${e.message}", e)
                }
        } catch (e: Exception) {
            Log.e("Firestore", "Excepción al registrar pago en Firestore", e)
        }
    }

    fun actualizarCantidadItemPedido(idPedido: String, idPlato: String, notas: String, nuevaCantidad: Int) {
        var pedidoModificado: Pedido? = null
        _pedidos.update { lista ->
            lista.map { pedido ->
                if (pedido.id == idPedido) {
                    val nuevosItems = if (nuevaCantidad <= 0) {
                        pedido.items.filterNot { it.idPlato == idPlato && it.notas == notas }
                    } else {
                        pedido.items.map { item ->
                            if (item.idPlato == idPlato && item.notas == notas) {
                                item.copy(cantidad = nuevaCantidad)
                            } else {
                                item
                            }
                        }
                    }
                    val nuevoTotal = nuevosItems.sumOf { it.precio * it.cantidad }
                    val nuevoEstado = if (nuevosItems.isEmpty()) EstadoPedido.CANCELADO else pedido.estado
                    val pedidoMod = pedido.copy(items = nuevosItems, precioTotal = nuevoTotal, estado = nuevoEstado)
                    pedidoModificado = pedidoMod
                    pedidoMod
                } else {
                    pedido
                }
            }
        }

        pedidoModificado?.let { mod ->
            try {
                val datosItems = mod.items.map { item ->
                    hashMapOf(
                        "idPlato" to item.idPlato,
                        "nombrePlato" to item.nombrePlato,
                        "cantidad" to item.cantidad,
                        "precio" to item.precio,
                        "notas" to item.notas
                    )
                }
                val actualizaciones = hashMapOf<String, Any>(
                    "items" to datosItems,
                    "precioTotal" to mod.precioTotal,
                    "estado" to mod.estado.name,
                    "nombreEstado" to mod.estado.nombreMostrado
                )
                firestore?.collection("orders")
                    ?.document("order_$idPedido")
                    ?.update(actualizaciones)
                    ?.addOnSuccessListener {
                        Log.d("Firestore", "✅ Pedido y total recalculado guardados en Firestore: $idPedido -> S/ ${mod.precioTotal}")
                    }
                    ?.addOnFailureListener { e ->
                        Log.e("Firestore", "❌ Error al actualizar items y total en Firestore: ${e.message}", e)
                    }
            } catch (e: Exception) {
                Log.e("Firestore", "Excepción al actualizar items en Firestore", e)
            }
        }
    }

    fun eliminarItemDePedido(idPedido: String, idPlato: String, notas: String) {
        actualizarCantidadItemPedido(idPedido, idPlato, notas, 0)
    }

    fun cancelarPedido(idPedido: String) {
        actualizarEstadoPedido(idPedido, EstadoPedido.CANCELADO)
    }
}
